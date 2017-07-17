package com.crossover.trial.journals.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.trial.journals.ApplicationTestConfig;

/**
 * Unit test class for {@link TemplateService}.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
public class TemplateServiceTest {

	private static final Object[] TEST_ARGS = { "login", "mail@domain.com", "j01", "jname", "pname", "c01", "cname" };

	@Autowired
	private TemplateService service;

	/**
	 * Tests scenarios consider getMessage method.
	 * 
	 */
	@Test
	public void testGetMessageString() {
		// WHEN non existent key THEN empty message
		assertThat(service.getMessage("none")).isEmpty();
		// WHEN existent key THEN not empty message
		assertThat(service.getMessage("app.url")).isNotEmpty();
	}

	/**
	 * Tests scenarios consider getMessage method.
	 * 
	 */
	@Test
	public void testGetMessageStringString() {
		// WHEN non existent key and default THEN default message
		assertThat(service.getMessage("none", "default")).isEqualTo("default");
		// WHEN existent key and default THEN not default message
		assertThat(service.getMessage("mail.journal.publish.body.text", "default")).isNotEqualTo("default");
	}

	/**
	 * Tests scenarios consider getMessage method.
	 * 
	 */
	@Test
	public void testGetMessageStringObjectArray() {
		// WHEN existent key and args THEN formatted message
		assertThat(service.getMessage("mail.journal.publish.body.text", TEST_ARGS)).contains((String) TEST_ARGS[6]);
	}

	/**
	 * Tests scenarios consider getMessage method.
	 * 
	 */
	@Test
	public void testGetMessageStringObjectArrayString() {
		// WHEN not existent key and args and default THEN default message
		assertThat(service.getMessage("none", TEST_ARGS, "default")).isEqualTo("default");
		// WHEN existent key and args THEN formatted message
		assertThat(service.getMessage("mail.journal.publish.body.text", TEST_ARGS, "default"))
				.contains((String) TEST_ARGS[6]);
	}

}
