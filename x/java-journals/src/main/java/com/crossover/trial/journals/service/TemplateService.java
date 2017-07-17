package com.crossover.trial.journals.service;

/**
 * Class created to handle templates, including some used to notify new journals
 * published.
 * 
 * @author marciofranca
 *
 */
public interface TemplateService {

	/**
	 * Returns message defined with informed code. If code not present, returns an
	 * empty string.
	 * 
	 * @param code
	 * @return
	 */
	String getMessage(String code);

	/**
	 * Returns message defined with informed code. If code not present, returns
	 * defaultMessage parameter.
	 * 
	 * @param code
	 * @param defaultMessage
	 * @return
	 */
	String getMessage(String code, String defaultMessage);

	/**
	 * Returns message defined with informed code, formatted with provided
	 * arguments. If code not present, returns an empty string.
	 * 
	 * @param code
	 * @param args
	 * @return
	 */
	String getMessage(String code, Object[] args);

	/**
	 * Returns message defined with informed code, formatted with provided
	 * arguments.If code not present, returns defaultMessage parameter.
	 * 
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @return
	 */
	String getMessage(String code, Object[] args, String defaultMessage);

}