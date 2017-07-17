package com.crossover.trial.journals.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.given;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.trial.journals.ApplicationTestConfig;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.CategoryRepository;
import com.crossover.trial.journals.repository.JournalRepository;
import com.crossover.trial.journals.repository.JournalRepositoryTest;
import com.crossover.trial.journals.repository.PublisherRepositoryTest;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import com.crossover.trial.journals.repository.UserRepository;
import com.crossover.trial.journals.repository.UserRepositoryTest;

/**
 * Unit test class for {@link JournalService}. It was implemented using mocking
 * strategy, to virtualize dependencies to repositories and underlying database
 * test data provisioning.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
public class JournalServiceTest {

	@Autowired
	private JournalService service;

	@MockBean
	private JournalRepository journalRepository;

	@MockBean
	private SubscriptionRepository subscriptionRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private CategoryRepository categoryRepository;

	private Date start;
	private Date end;
	private List<Journal> journals;
	private User user;
	private Publisher publisher;
	private Journal journal;
	private User otherUser;
	private Journal otherJournal;
	private Publisher otherPublisher;

	/**
	 * Test data generated for different test conditions required by scenarios.
	 * 
	 */
	@Before
	public void setUp() {
		// GIVEN user subscribed to 2 categories with 3 journals published
		start = JournalRepositoryTest.createStartDate();
		end = JournalRepositoryTest.createEndDate();
		journals = new ArrayList<>();
		journals.add(createJournal("journal1", 1L, 1L));
		journals.add(createJournal("journal2", 2L, 2L));
		journals.add(createJournal("journal3", 3L, 1L));
		given(journalRepository.findByPublishDateBetween(start, end)).willReturn(journals);

		user = createUser("user1");
		List<Subscription> subscriptions = new ArrayList<>();
		Set<Category> categories = new HashSet<>();
		journals.stream().forEach(journal -> categories.add(journal.getCategory()));
		Set<Long> ids = new HashSet<>();
		categories.stream().forEach(category -> ids.add(category.getId()));
		categories.stream().forEach(category -> subscriptions.add(createSubscription(category, user)));
		user.setSubscriptions(subscriptions);
		given(subscriptionRepository.findByCategoryIdInOrderByUserId(ids)).willReturn(subscriptions);

		// GIVEN publisher with 3 journals published
		publisher = createPublisher("publisher1", 2L);
		given(journalRepository.findByPublisher(publisher)).willReturn(journals);

		// GIVEN user with subscriptions to 2 categories
		given(userRepository.findOne(user.getId())).willReturn(user);

		// GIVEN 2 categories with 3 journals
		given(journalRepository.findByCategoryIdIn(ids)).willReturn(journals);

		// GIVEN journal
		journal = journals.get(0);
		journal.setPublisher(publisher);
		given(journalRepository.findOne(journal.getId())).willReturn(journal);
		given(journalRepository.save(journal)).willReturn(journal);

		// GIVEN publisher with 1 journal
		given(userRepository.findOne(publisher.getUser().getId())).willReturn(publisher.getUser());

		// GIVEN user not subscribed
		otherUser = createUser("other");
		otherUser.setId(10L);
		given(userRepository.findOne(otherUser.getId())).willReturn(otherUser);

		// GIVEN category
		given(categoryRepository.findOne(journal.getCategory().getId())).willReturn(journal.getCategory());

		// GIVEN invalid journal
		otherJournal = createJournal("other", 10L, 1L);
		given(journalRepository.save(otherJournal)).willThrow(new DataIntegrityViolationException("Data violation"));

		// GIVEN invalid publisher
		otherPublisher = createPublisher("other", 10L);
	}

	/**
	 * Tests scenarios consider findNewPublishedJournals method.
	 * 
	 */
	@Test
	public void testFindNewPublishedJournals() {
		// GIVEN user subscribed to 2 categories with 3 journals published
		// WHEN find new published journals per user THEN user and 3 journals
		Map<User, List<Journal>> users = service.findNewPublishedJournals(start, end);
		assertThat(users).hasSize(1).containsKey(user);
		assertThat(users.get(user)).hasSize(3).containsAll(journals);
	}

	/**
	 * Tests scenarios consider notifyNewPublishedJournals method.
	 * 
	 */
	@Test
	public void testNotifyNewPublishedJournals() {
		// GIVEN user subscribed to 2 categories with 3 journals published
		// WHEN find new published journals per user THEN user and 3 journals
		service.notifyNewPublishedJournals(start, end);

		await().atMost(10, TimeUnit.SECONDS).until(() -> MessageListener.COUNT.get() == 0);
	}

	/**
	 * Tests scenarios consider publisherList method.
	 * 
	 */
	@Test
	public void testPublisherList() {
		// GIVEN publisher with 3 journals published
		// WHEN find journals per publisher THEN 3 journals
		assertThat(service.publisherList(publisher)).hasSize(3).containsAll(journals);
	}

	/**
	 * Tests scenarios consider listAll method.
	 * 
	 */
	@Test
	public void testListAll() {
		// GIVEN user with subscriptions to 2 categories with 3 journals
		// WHEN list journals per user THEN 3 journals
		assertThat(service.listAll(user)).hasSize(3).containsAll(journals);

		// GIVEN user not subscribed
		// WHEN list journals per user THEN none
		assertThat(service.listAll(otherUser)).isEmpty();
	}

	/**
	 * Tests scenarios consider findJournalIfHasAccess method.
	 * 
	 */
	@Test
	public void testFindJournalIfHasAccess() {
		Optional<Journal> result;
		// GIVEN user subscribed
		// WHEN find journal with access THEN journal
		result = service.findJournalIfHasAccess(journal.getId(), user.getId());
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get()).isEqualTo(journal);

		// GIVEN publisher
		// WHEN find journal with access THEN journal
		result = service.findJournalIfHasAccess(journal.getId(), publisher.getUser().getId());
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get()).isEqualTo(journal);

		// GIVEN user not subscribed
		// WHEN find journal with access THEN null
		result = service.findJournalIfHasAccess(journal.getId(), otherUser.getId());
		assertThat(result.isPresent()).isFalse();
	}

	/**
	 * Tests scenarios consider publish method.
	 * 
	 */
	@Test
	public void testPublish() {
		InputStream input;
		Journal result;

		// GIVEN a new journal
		// WHEN publish journal THEN ok
		input = new ByteArrayInputStream(journal.getName().getBytes());
		result = service.publish(publisher, journal, journal.getCategory().getId(), input);
		assertThat(result).isNotNull().isEqualTo(journal);

		// GIVEN integrity violation
		// WHEN publish journal THEN error
		final InputStream input2 = new ByteArrayInputStream(journal.getName().getBytes());
		assertThatThrownBy(() -> {
			service.publish(publisher, otherJournal, otherJournal.getCategory().getId(), input2);
		}).isInstanceOf(ServiceException.class).hasMessageContaining("Data violation");

		// GIVEN a new journal in not existent category
		// WHEN publish journal THEN error
		final InputStream input3 = new ByteArrayInputStream(journal.getName().getBytes());
		assertThatThrownBy(() -> {
			service.publish(publisher, journal, 10L, input3);
		}).isInstanceOf(ServiceException.class).hasMessageContaining("Category not found");

	}

	/**
	 * Tests scenarios consider unPublish method.
	 * 
	 */
	@Test
	public void testUnPublish() {
		// GIVEN a new journal
		// WHEN unpublish journal THEN ok
		service.unPublish(publisher, journal.getId());

		// GIVEN integrity violation
		// WHEN unpublish journal THEN error
		assertThatThrownBy(() -> {
			service.unPublish(publisher, otherJournal.getId());
		}).isInstanceOf(ServiceException.class).hasMessageContaining("Journal doesn't exist");

		// GIVEN not publisher
		// WHEN unpublish journal THEN error
		assertThatThrownBy(() -> {
			service.unPublish(otherPublisher, journal.getId());
		}).isInstanceOf(ServiceException.class).hasMessageContaining("Journal cannot be removed");
	}

	/**
	 * Helper method for User test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	public static User createUser(String key) {
		User example = UserRepositoryTest.createExample(key);
		example.setId(1L);
		return example;
	}

	/**
	 * Helper method for Journal test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @param journalId
	 *            journal id to be assigned to object
	 * @param categoryId
	 *            category id to be assigned to object
	 * @return
	 */
	public static Journal createJournal(String key, Long journalId, Long categoryId) {
		Journal example = JournalRepositoryTest.createExample(key);
		example.setId(journalId);
		example.getCategory().setId(categoryId);
		return example;
	}

	/**
	 * Helper method for Subscription test data creation.
	 * 
	 * @param category
	 *            category to be assigned to object
	 * @param user
	 *            user to be assigned to object
	 * @return
	 */
	public static Subscription createSubscription(Category category, User user) {
		Subscription example = new Subscription();
		example.setId(1L);
		example.setCategory(category);
		example.setUser(user);
		example.setDate(Calendar.getInstance().getTime());
		return example;
	}

	/**
	 * Helper method for Publisher test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @param id
	 *            id to be assigned to object
	 * @return
	 */
	public static Publisher createPublisher(String key, Long id) {
		Publisher example = PublisherRepositoryTest.createExample(key);
		example.setId(id);
		example.getUser().setId(id);
		return example;
	}

}
