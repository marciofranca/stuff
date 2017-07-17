package com.crossover.trial.journals.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
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
import com.crossover.trial.journals.model.Subscription;

/**
 * Unit test class for {@link SubscriptionRepository}. Current configurations
 * are being used to run test scenarios in memory database using injected
 * {@link TestEntityManager}.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
@DataJpaTest
public class SubscriptionRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SubscriptionRepository repository;

	/**
	 * Tests scenarios consider initial state for {@link SubscriptionRepository} in
	 * database, testing CRUD operations.
	 */
	@Test
	public void testInsertFirst() {
		String key = UUID.randomUUID().toString();
		Subscription example;
		long count;
		Iterable<Subscription> result;

		// GIVEN empty table
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();

		// GIVEN first row
		example = createExample(key);
		entityManager.persist(example.getUser());
		entityManager.persist(example.getCategory());
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
		example.setDate(Calendar.getInstance().getTime());
		result.iterator().next().setDate(example.getDate());

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
	 * Tests scenarios consider method findByCategory.
	 * 
	 */
	@Test
	public void testFindByCategory() {
		String key = UUID.randomUUID().toString();
		Subscription example;
		long count;
		Iterable<Subscription> result;

		// GIVEN empty table
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();

		// GIVEN first row
		example = createExample(key);
		entityManager.persist(example.getUser());
		entityManager.persist(example.getCategory());
		entityManager.persist(example);
		entityManager.flush();

		// WHEN count
		count = repository.count();
		// THEN one
		assertThat(count).isEqualTo(1);

		// WHEN find all
		result = repository.findByCategory(example.getCategory());
		// THEN example
		assertThat(result).hasSize(1);
		assertThat(result.iterator().next()).isEqualToComparingFieldByFieldRecursively(example);
	}

	/**
	 * Tests scenarios consider method findByCategoryIdInOrderByUserId.
	 * 
	 */
	@Test
	public void testFindByCategoryIdIn() {
		String key = UUID.randomUUID().toString();
		Subscription example;
		long count;
		Iterable<Subscription> result;

		// GIVEN empty table
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();

		// GIVEN first row
		example = createExample(key);
		entityManager.persist(example.getUser());
		entityManager.persist(example.getCategory());
		entityManager.persist(example);
		entityManager.flush();

		// WHEN count
		count = repository.count();
		// THEN one
		assertThat(count).isEqualTo(1);

		// WHEN find all
		Set<Long> ids = new HashSet<Long>();
		ids.add(example.getCategory().getId());
		ids.add(example.getCategory().getId());
		result = repository.findByCategoryIdInOrderByUserId(ids);
		// THEN example
		assertThat(result).hasSize(1);
		assertThat(result.iterator().next()).isEqualToComparingFieldByFieldRecursively(example);
	}

	/**
	 * Helper method for Subscription test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	public static Subscription createExample(String key) {
		Subscription example = new Subscription();
		example.setId(null);
		example.setCategory(CategoryRepositoryTest.createExample(key));
		example.setUser(UserRepositoryTest.createExample(key));
		example.setDate(Calendar.getInstance().getTime());

		return example;
	}

}
