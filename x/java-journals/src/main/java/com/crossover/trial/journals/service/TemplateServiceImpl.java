package com.crossover.trial.journals.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/**
 * Class created to handle templates, including some used to notify new journals
 * published.
 * 
 * @author marciofranca
 *
 */
@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private MessageSource messageSource;

	private MessageSourceAccessor accessor;

	@PostConstruct
	private void init() {
		accessor = new MessageSourceAccessor(messageSource);
	}

	/**
	 * Returns message defined with informed code. If code not present, returns an
	 * empty string.
	 * 
	 * @param code
	 * @return
	 */
	@Override
	public String getMessage(String code) {
		return getMessage(code, "");
	}

	/**
	 * Returns message defined with informed code. If code not present, returns
	 * defaultMessage parameter.
	 * 
	 * @param code
	 * @param defaultMessage
	 * @return
	 */
	@Override
	public String getMessage(String code, String defaultMessage) {
		return accessor.getMessage(code, defaultMessage);
	}

	/**
	 * Returns message defined with informed code, formatted with provided
	 * arguments. If code not present, returns an empty string.
	 * 
	 * @param code
	 * @param args
	 * @return
	 */
	@Override
	public String getMessage(String code, Object[] args) {
		return getMessage(code, args, "");
	}

	/**
	 * Returns message defined with informed code, formatted with provided
	 * arguments.If code not present, returns defaultMessage parameter.
	 * 
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @return
	 */
	@Override
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return accessor.getMessage(code, args, defaultMessage);
	}
}
