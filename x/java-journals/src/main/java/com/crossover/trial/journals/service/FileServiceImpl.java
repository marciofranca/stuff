package com.crossover.trial.journals.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.crossover.trial.journals.Application;

/**
 * This class implements required logic to store and retrieve files.
 * 
 * @author marciofranca
 *
 */
@Service
public class FileServiceImpl implements FileService {
	private static final Logger LOG = Logger.getLogger(FileServiceImpl.class);

	/**
	 * Save a new file to the bucket.
	 * 
	 * @param bucket
	 *            identifier used to group files in same folder structure (e.g.:
	 *            category id).
	 * @param input
	 *            InputStream with file content. Should be closed by caller once is
	 *            finished.
	 * @return UUID key generated for later retrieval.
	 * 
	 */
	@Override
	public String saveFile(Object bucket, InputStream input) {
		if (input == null) {
			throw new ServiceException("File is required.");
		}
		String uuid = UUID.randomUUID().toString();
		File dir = new File(getDirectory(bucket));
		createDirectoryIfNotExist(dir);

		File outputFile = new File(getFileName(bucket, uuid));

		try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			FileCopyUtils.copy(input, output);
		} catch (IOException e) {
			LOG.error("Error saving file: " + outputFile.getAbsolutePath(), e);
			throw new ServiceException("Error saving file: " + outputFile.getAbsolutePath(), e);
		}
		return uuid;

	}

	/**
	 * Delete some existing file.
	 * 
	 * @param bucket
	 *            same value informed in file creation.
	 * @param uuid
	 *            sale value returned in file creation.
	 */
	@Override
	public void deleteFile(Object bucket, String uuid) {
		String filePath = getFileName(bucket, uuid);
		File file = new File(filePath);
		if (file.exists()) {
			boolean deleted = file.delete();
			if (!deleted) {
				LOG.error("File " + filePath + " cannot be deleted");
			}
		}
	}

	/**
	 * Retrieve some existing file content.
	 * 
	 * @param bucket
	 *            same value informed in file creation.
	 * @param uuid
	 *            sale value returned in file creation.
	 * @return InputStream with file content. Should be closed by caller once is
	 *         finished.
	 */
	@Override
	public InputStream readFile(Object bucket, String uuid) {
		File inputFile = new File(getFileName(bucket, uuid));
		try {
			return new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			LOG.error("Error reading file: " + inputFile.getAbsolutePath(), e);
			throw new ServiceException("Error reading file: " + inputFile.getAbsolutePath(), e);
		}
	}

	private boolean createDirectoryIfNotExist(File dir) {
		if (!dir.exists()) {
			boolean created = dir.mkdirs();
			if (!created) {
				return false;
			}
		}
		return true;
	}

	public static String getFileName(Object bucket, String uuid) {
		return getDirectory(bucket) + "/" + uuid + ".pdf";
	}

	public static String getDirectory(Object bucket) {
		return Application.ROOT + "/" + bucket;
	}

}
