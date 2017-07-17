package com.crossover.trial.journals.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Unit test class for {@link Category}, in order to tests overrinding of
 * hashCode and equals methods.
 * 
 * @author marciofranca
 *
 */
public class CategoryTest {

	/**
	 * Tests scenarios consider hashCode method.
	 * 
	 */
	@Test
	public void testHashCode() {
		// WHEN null id THEN zero hash
		Category example = new Category();
		assertThat(example.hashCode()).isZero();

		// WHEN not null id THEN not zero hash and id hash
		example.setId(1L);
		assertThat(example.hashCode()).isNotZero().isEqualTo(example.getId().hashCode());
	}

	/**
	 * Tests scenarios consider equals method.
	 * 
	 */
	@Test
	public void testEqualsObject() {
		// WHEN not null and other null THEN not equal
		Category example = new Category();
		Object other = null;
		assertThat(example).isNotEqualTo(other);

		// WHEN not null and other not Category THEN not equal
		other = new Object();
		assertThat(example).isNotEqualTo(other);

		// WHEN id null and other id null THEN equal
		other = new Category();
		assertThat(example).isEqualTo(other);

		// WHEN id not null and other id null THEN not equal
		example.setId(1L);
		assertThat(example).isNotEqualTo(other);

		// WHEN id = other id THEN equal
		((Category) other).setId(1L);
		assertThat(example).isEqualTo(other);
	}

}
