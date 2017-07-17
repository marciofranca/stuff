package com.crossover.trial.journals.service;

import java.io.InputStream;

/**
 * This class implements required logic to store and retrieve files.
 * 
 * @author marciofranca
 *
 */
public interface FileService {

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
	String saveFile(Object bucket, InputStream input);

	/**
	 * Delete some existing file.
	 * 
	 * @param bucket
	 *            same value informed in file creation.
	 * @param uuid
	 *            sale value returned in file creation.
	 */
	InputStream readFile(Object bucket, String uuid);

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
	void deleteFile(Object bucket, String uuid);
}
