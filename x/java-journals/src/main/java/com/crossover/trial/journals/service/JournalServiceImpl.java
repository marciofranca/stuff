package com.crossover.trial.journals.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.CategoryRepository;
import com.crossover.trial.journals.repository.JournalRepository;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import com.crossover.trial.journals.repository.UserRepository;

@Service
public class JournalServiceImpl implements JournalService {

	@Autowired
	private JournalRepository journalRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	private FileService fileService;

	@Override
	public List<Journal> listAll(User user) {
		User persistentUser = userRepository.findOne(user.getId());
		List<Subscription> subscriptions = persistentUser.getSubscriptions();
		if (subscriptions != null && !subscriptions.isEmpty()) {
			Set<Long> ids = new HashSet<>(subscriptions.size());
			subscriptions.stream().forEach(s -> ids.add(s.getCategory().getId()));
			return journalRepository.findByCategoryIdIn(ids);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<Journal> publisherList(Publisher publisher) {
		Iterable<Journal> journals = journalRepository.findByPublisher(publisher);
		return StreamSupport.stream(journals.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public Journal publish(Publisher publisher, Journal journal, Long categoryId, InputStream input) {
		String bucket = String.valueOf(publisher.getId());
		String uuid = fileService.saveFile(bucket, input);
		journal.setUuid(uuid);

		Category category = categoryRepository.findOne(categoryId);
		if (category == null) {
			throw new ServiceException("Category not found");
		}
		journal.setPublisher(publisher);
		journal.setCategory(category);
		try {
			Journal result = journalRepository.save(journal);

			notifyNewPublishedJournal(result);

			return result;
		} catch (DataIntegrityViolationException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void unPublish(Publisher publisher, Long id) {
		Journal journal = journalRepository.findOne(id);
		if (journal == null) {
			throw new ServiceException("Journal doesn't exist");
		}
		if (!journal.getPublisher().getId().equals(publisher.getId())) {
			throw new ServiceException("Journal cannot be removed");
		}
		fileService.deleteFile(publisher.getId(), journal.getUuid());
		journalRepository.delete(journal);
	}

	@Override
	public Map<User, List<Journal>> findNewPublishedJournals(Date start, Date end) {
		List<Journal> newJournals = journalRepository.findByPublishDateBetween(start, end);

		Map<Long, List<Journal>> categories = new HashMap<>();
		newJournals.stream().forEach(journal -> add(categories, journal));

		List<Subscription> subscriptions = subscriptionRepository.findByCategoryIdInOrderByUserId(categories.keySet());

		Map<User, List<Journal>> users = new HashMap<>();
		subscriptions.stream().forEach(subscription -> add(users, categories, subscription));

		return users;
	}

	private void notifyNewPublishedJournal(Journal journal) {
		Collection<Subscription> subscriptions = subscriptionRepository.findByCategory(journal.getCategory());
		subscriptions.stream().forEach(s -> messageService.sendJournalPublishMessage(s.getUser(), journal));
	}

	@Override
	public void notifyNewPublishedJournals(Date start, Date end) {
		Map<User, List<Journal>> users = findNewPublishedJournals(start, end);
		users.forEach((user, journals) -> messageService.sendNewJournalsMessage(user, journals));
	}

	private void add(Map<Long, List<Journal>> map, Journal journal) {
		Long category = journal.getCategory().getId();
		List<Journal> journals;
		if (map.containsKey(category)) {
			journals = map.get(category);
		} else {
			journals = new ArrayList<>();
			map.put(category, journals);
		}
		journals.add(journal);
	}

	private void add(Map<User, List<Journal>> map, Map<Long, List<Journal>> categories, Subscription subscription) {
		User user = subscription.getUser();
		List<Journal> journals;
		if (map.containsKey(user)) {
			journals = map.get(user);
		} else {
			journals = new ArrayList<>();
			map.put(user, journals);
		}
		List<Journal> added = categories.get(subscription.getCategory().getId());
		if (added != null) {
			journals.addAll(added);
		}

	}

	@Override
	public Optional<Journal> findJournalIfHasAccess(Long journalId, Long userId) {
		Journal journal = journalRepository.findOne(journalId);

		User user = userRepository.findOne(userId);
		if (!journal.getPublisher().getId().equals(user.getId())) {
			Category category = journal.getCategory();

			List<Subscription> subscriptions = user.getSubscriptions();
			Optional<Subscription> subscription = subscriptions.stream()
					.filter(s -> s.getCategory().getId().equals(category.getId())).findFirst();

			if (!subscription.isPresent()) {
				journal = null;
			}
		}
		return Optional.ofNullable(journal);
	}
}
