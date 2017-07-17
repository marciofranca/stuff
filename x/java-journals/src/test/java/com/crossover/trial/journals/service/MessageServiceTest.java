package com.crossover.trial.journals.service;

import static org.awaitility.Awaitility.await;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.trial.journals.ApplicationTestConfig;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.JournalRepositoryTest;
import com.crossover.trial.journals.repository.UserRepositoryTest;

/**
 * Unit test class for {@link MessageService}.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
public class MessageServiceTest {

	@Autowired
	private MessageService service;

	/**
	 * Tests scenarios consider sendJournalPublishMessage method.
	 * 
	 */
	@Test
	public void testSendJournalPublishMessage() {
		User user = createUser("user1");
		Journal journal = createJournal("journal1");

		service.sendJournalPublishMessage(user, journal);

		await().atMost(10, TimeUnit.SECONDS).until(() -> MessageListener.COUNT.get() == 0);
	}

	/**
	 * Tests scenarios consider sendNewJournalsMessage method.
	 * 
	 */
	@Test
	public void testSendNewJournalsMessage() {
		User user = createUser("user1");

		List<Journal> journals = new ArrayList<>();
		journals.add(createJournal("journal1"));
		journals.add(createJournal("journal2"));

		service.sendNewJournalsMessage(user, journals);

		await().atMost(10, TimeUnit.SECONDS).until(() -> MessageListener.COUNT.get() == 0);
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
	 * @return
	 */
	public static Journal createJournal(String key) {
		Journal example = JournalRepositoryTest.createExample(key);
		example.setId(1L);
		return example;
	}

}
