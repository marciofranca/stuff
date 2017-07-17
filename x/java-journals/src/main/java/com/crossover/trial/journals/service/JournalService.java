package com.crossover.trial.journals.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;

public interface JournalService {

	List<Journal> listAll(User user);

	List<Journal> publisherList(Publisher publisher);

	Journal publish(Publisher publisher, Journal journal, Long categoryId, InputStream input);

	void unPublish(Publisher publisher, Long journalId);

	Map<User, List<Journal>> findNewPublishedJournals(Date start, Date end);

	void notifyNewPublishedJournals(Date start, Date end);
	
	Optional<Journal> findJournalIfHasAccess(Long journalId, Long userId);
}
