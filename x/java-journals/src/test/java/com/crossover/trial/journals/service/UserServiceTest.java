package com.crossover.trial.journals.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.trial.journals.ApplicationTestConfig;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.CategoryRepository;
import com.crossover.trial.journals.repository.CategoryRepositoryTest;
import com.crossover.trial.journals.repository.UserRepository;
import com.crossover.trial.journals.repository.UserRepositoryTest;

/**
 * Unit test class for {@link UserService}. It was implemented using mocking
 * strategy, to virtualize dependencies to repositories and underlying database
 * test data provisioning.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
public class UserServiceTest {

	@Autowired
	private UserService service;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private CategoryRepository categoryRepository;

	private User user;
	private Category category;

	/**
	 * Test data generated for different test conditions required by scenarios.
	 * 
	 */
	@Before
	public void setUp() {
		// GIVEN existing category
		category = createCategory("category1");
		given(categoryRepository.findOne(category.getId())).willReturn(category);

		// GIVEN existing category
		user = createUser("user1");

		given(userRepository.save(user)).willReturn(user);

	}

	/**
	 * Tests scenarios consider subscribe method.
	 * 
	 */
	@Test
	public void testSubscribe() {
		// GIVEN user not subscribed
		// WHEN subscribe in a existing category THEN ok
		service.subscribe(user, category.getId());

		// GIVEN user not subscribed
		// WHEN subscribe in a not existing category THEN erro
		assertThatThrownBy(() -> {
			service.subscribe(user, 10L);
		}).isInstanceOf(ServiceException.class).hasMessageContaining("Category not found");

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
		example.setSubscriptions(null);
		return example;
	}

	/**
	 * Helper method for Category test data creation.
	 * 
	 * @param key
	 *            string to be used in this object identification.
	 * @return
	 */
	public static Category createCategory(String key) {
		Category example = CategoryRepositoryTest.createExample(key);
		example.setId(1L);
		return example;
	}

}
