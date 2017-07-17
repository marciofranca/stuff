package com.crossover.trial.journals.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.repository.CategoryRepositoryTest;

/**
 * Unit test class for {@link SubscriptionDTO}, in order to tests overrinding of
 * hashCode and equals methods.
 * 
 * @author marciofranca
 *
 */
public class SubscriptionDTOTest {

	/**
	 * Tests scenarios consider equals method.
	 * 
	 */
	@Test
	public void testEqualsObject() {
		Category category = createCategory("category1");

		// WHEN not null and other null THEN not equal
		SubscriptionDTO example = new SubscriptionDTO(category);
		Object other = null;
		assertThat(example).isNotEqualTo(other);

		// WHEN not null and other not category THEN not equal
		other = new Object();
		assertThat(example).isNotEqualTo(other);

		// WHEN id null and other id null THEN equal
		other = new SubscriptionDTO(category);
		assertThat(example).isEqualTo(other);

		// WHEN id not null and other id null THEN not equal
		example.setId(2L);
		assertThat(example).isNotEqualTo(other);

		// WHEN id = other id THEN equal
		((SubscriptionDTO) other).setId(2L);
		assertThat(example).isEqualTo(other);

		// WHEN myself THEN equal
		assertThat(example).isEqualTo(example);

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
