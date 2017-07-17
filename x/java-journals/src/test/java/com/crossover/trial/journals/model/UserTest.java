package com.crossover.trial.journals.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.crossover.trial.journals.service.FileService;

/**
 * Unit test class for {@link User}, in order to tests overrinding of hashCode
 * and equals methods.
 * 
 * @author marciofranca
 *
 */
public class UserTest {

	/**
	 * Tests scenarios consider hashCode method.
	 * 
	 */
	@Test
	public void testHashCode() {
		// WHEN null id THEN zero hash
		User example = new User();
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
		User example = new User();
		Object other = null;
		assertThat(example).isNotEqualTo(other);

		// WHEN not null and other not user THEN not equal
		other = new Object();
		assertThat(example).isNotEqualTo(other);

		// WHEN id null and other id null THEN equal
		other = new User();
		assertThat(example).isEqualTo(other);

		// WHEN id not null and other id null THEN not equal
		example.setId(1L);
		assertThat(example).isNotEqualTo(other);

		// WHEN id = other id THEN equal
		((User) other).setId(1L);
		assertThat(example).isEqualTo(other);
	}

}
