package com.crossover.trial.journals.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.crossover.trial.journals.model.Category;

/**
 * Unit test class for {@link CategoryRepository}. Current configurations are
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
public class CategoryRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CategoryRepository repository;

	/**
	 * Tests scenarios consider initial state for {@link CategoryRepository} in
	 * database, testing CRUD operations.
	 */
	@Test
	public void testInsertFirst() {
		String key = UUID.randomUUID().toString();
		Category example;
		long count;
		Iterable<Category> result;

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
	 * Helper method for Category test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	public static Category createExample(String key) {
		Category example = new Category();
		example.setId(null);
		example.setName("Category " + key);

		return example;
	}

}
