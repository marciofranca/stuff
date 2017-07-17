package com.crossover.trial.journals.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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
import com.crossover.trial.journals.model.Role;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;

/**
 * Unit test class for {@link UserRepository}. Current configurations are being
 * used to run test scenarios in memory database using injected
 * {@link TestEntityManager}.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository repository;

	/**
	 * Tests scenarios consider initial state for {@link UserRepository} in
	 * database, testing CRUD operations.
	 */
	@Test
	public void testInsertFirst() {
		String key = UUID.randomUUID().toString();
		User example;
		long count;
		Iterable<User> result;

		// GIVEN empty table
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();

		// GIVEN first row
		example = createExample(key);
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
		example.setLoginName(example.getLoginName() + " UPDATED");
		result.iterator().next().setLoginName(example.getLoginName());

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
	 * Tests scenarios consider method findByLoginName.
	 * 
	 */
	@Test
	public void testFindByLoginName() {
		String key = UUID.randomUUID().toString();
		User example;
		long count;
		User result;

		// GIVEN empty table
		// WHEN count
		count = repository.count();
		// THEN empty
		assertThat(count).isZero();

		// GIVEN first row
		example = createExample(key);
		entityManager.persist(example);
		entityManager.flush();

		// WHEN count
		count = repository.count();
		// THEN one
		assertThat(count).isEqualTo(1);

		// WHEN find by loginname
		result = repository.findByLoginName(example.getLoginName());
		// THEN example
		assertThat(result).isNotNull().isEqualTo(example).isEqualToComparingFieldByFieldRecursively(example);
	}

	/**
	 * Helper method for User test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	public static User createExample(String key) {
		User example = new User();
		example.setId(null);
		example.setLoginName("User " + key);
		example.setEmail("msfranca+" + key + "@gmail.com");
		example.setEnabled(true);
		example.setPwd("pwd");
		example.setRole(Role.USER);
		example.setSubscriptions(new ArrayList<Subscription>());

		return example;
	}

}
