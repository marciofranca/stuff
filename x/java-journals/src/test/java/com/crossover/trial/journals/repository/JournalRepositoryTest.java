package com.crossover.trial.journals.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.trial.journals.ApplicationTestConfig;
import com.crossover.trial.journals.model.Journal;

/**
 * Unit test class for {@link JournalRepository}. Current configurations are
 * being used to run test scenarios in memory database using injected
 * {@link TestEntityManager}.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
@DataJpaTest
public class JournalRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private JournalRepository repository;

	/**
	 * Tests scenarios consider initial state for {@link JournalRepository} in
	 * database, testing CRUD operations.
	 */
	@Test
	public void testInsertFirst() {
		String key = UUID.randomUUID().toString();
		Journal example;
		long count;
		Iterable<Journal> result;

		// GIVEN empty table
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();

		// GIVEN first row
		example = createExample(key);
		entityManager.persist(example.getCategory());
		entityManager.persist(example.getPublisher().getUser());
		entityManager.persist(example.getPublisher());
		entityManager.persist(example);
		entityManager.flush();

		// WHEN count
		count = repository.count();
		// THEN one
		assertThat(count).isEqualTo(1);

		// WHEN find all
		result = repository.findAll();
		// THEN example
		assertThat(result).hasSize(1);
		assertThat(result.iterator().next()).isEqualToComparingFieldByFieldRecursively(example);

		// GIVEN row updated
		example.setName(example.getName() + " UPDATED");
		result.iterator().next().setName(example.getName());
		entityManager.persist(example);
		entityManager.flush();

		// WHEN count
		count = repository.count();
		// THEN one
		assertThat(count).isEqualTo(1);

		// WHEN find all
		result = repository.findAll();
		// THEN example
		assertThat(result).hasSize(1);
		assertThat(result.iterator().next()).isEqualToComparingFieldByFieldRecursively(example);

		// GIVEN row removed
		entityManager.remove(result.iterator().next());
		entityManager.flush();
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();
	}

	/**
	 * Tests scenarios consider method findByPublishDateBetween.
	 * 
	 */
	@Test
	public void testFindByPublishDateBetween() {
		String key = UUID.randomUUID().toString();
		Journal example;
		long count;
		Iterable<Journal> result;

		// GIVEN empty table
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();

		// GIVEN first row
		example = createExample(key);
		entityManager.persist(example.getCategory());
		entityManager.persist(example.getPublisher().getUser());
		entityManager.persist(example.getPublisher());
		entityManager.persist(example);
		entityManager.flush();

		// WHEN count
		count = repository.count();
		// THEN one
		assertThat(count).isEqualTo(1);

		// GIVEN published yesterday
		key = UUID.randomUUID().toString();
		example = createExample(key);
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		example.setPublishDate(yesterday.getTime());
		entityManager.persist(example.getCategory());
		entityManager.persist(example.getPublisher().getUser());
		entityManager.persist(example.getPublisher());
		entityManager.persist(example);
		entityManager.flush();

		// WHEN find by publish date yesterday
		result = repository.findByPublishDateBetween(createStartDate(), createEndDate());
		// THEN example
		assertThat(result).hasSize(1);
		assertThat(result.iterator().next()).isEqualToComparingFieldByFieldRecursively(example);

	}

	/**
	 * Helper method for Journal test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	public static Journal createExample(String key) {
		Journal example = new Journal();
		example.setId(null);
		example.setUuid(key);
		example.setName("Journal " + key);
		example.setCategory(CategoryRepositoryTest.createExample(key));
		example.setPublisher(PublisherRepositoryTest.createExample(key));

		return example;
	}

	/**
	 * Helper method for start date test data creation. It generates a Date object
	 * for yesterday at 00:00:00.000.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	// Yesterday 00:00:00.000
	public static Date createStartDate() {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.MILLISECOND, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.HOUR, 0);
		startCalendar.add(Calendar.DAY_OF_MONTH, -1);
		return startCalendar.getTime();
	}

	/**
	 * Helper method for end date test data creation. It generates a Date object for
	 * yesterday 23:59:59.999.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	public static Date createEndDate() {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.set(Calendar.SECOND, 0);
		endCalendar.set(Calendar.MINUTE, 0);
		endCalendar.set(Calendar.HOUR, 0);
		endCalendar.add(Calendar.MILLISECOND, -1);

		return endCalendar.getTime();
	}

}
