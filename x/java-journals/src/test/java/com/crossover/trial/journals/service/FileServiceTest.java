package com.crossover.trial.journals.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.trial.journals.ApplicationTestConfig;

/**
 * Unit test class for {@link FileService}.
 * 
 * @author marciofranca
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-unit.properties")
public class FileServiceTest {

	@Autowired
	private FileService service;

	/**
	 * Tests scenarios consider saveFile method.
	 * 
	 */
	@Test
	public void testSaveFile() {
		String bucket = UUID.randomUUID().toString();

		InputStream input = new ByteArrayInputStream(bucket.getBytes());
		String uuid = service.saveFile(bucket, input);
		Assert.assertFalse(uuid == null || uuid.isEmpty());
		service.deleteFile(bucket, uuid);
	}

	/**
	 * Tests scenarios consider readFile method.
	 * 
	 */
	@Test
	public void testReadFile() {
		String bucket = UUID.randomUUID().toString();
		InputStream input = new ByteArrayInputStream(bucket.getBytes());
		String uuid = service.saveFile(bucket, input);
		Assert.assertFalse(uuid == null || uuid.isEmpty());

		input = service.readFile(bucket, uuid);

		StringWriter output = new StringWriter();
		try {
			IOUtils.copy(input, output);
		} catch (IOException e) {
		}
		Assert.assertEquals("Read content", bucket, output.toString());

	}

	/**
	 * Tests scenarios consider saveFile method.
	 * 
	 */
	@Test
	public void testDeleteFile() {
		String bucket = UUID.randomUUID().toString();

		InputStream input = new ByteArrayInputStream(bucket.getBytes());
		String uuid = service.saveFile(bucket, input);
		Assert.assertFalse(uuid == null || uuid.isEmpty());
		service.deleteFile(bucket, uuid);
	}

}
