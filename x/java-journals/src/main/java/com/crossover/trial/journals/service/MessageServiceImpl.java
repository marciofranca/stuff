package com.crossover.trial.journals.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.dto.MessageDTO;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;

/**
 * This class implements required logic to send notification messages as
 * requested by trial requirements.
 * 
 * @author marciofranca
 *
 */
@Service
public class MessageServiceImpl implements MessageService {
	private static final Logger LOG = Logger.getLogger(MessageServiceImpl.class);

	private static final String MAIL_SUBJECT_SUFFIX = ".subject";
	private static final String MAIL_TEXT_SUFFIX = ".body.text";
	private static final String MAIL_ITEM_SUFFIX = ".body.item";

	private static final String MAIL_EVENT_JOURNAL_PUBLISH = "mail.journal.publish";
	private static final String MAIL_EVENT_NEW_JOURNALS = "mail.new.journals";

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private TemplateService template;

	/**
	 * Send a message by email for subscribed user when a new journal is published.
	 */
	@Override
	public void sendJournalPublishMessage(User user, Journal journal) {
		MessageDTO message = new MessageDTO();
		message.setEmail(user.getEmail());

		Object[] args = { user.getLoginName(), user.getEmail(), journal.getId(), journal.getName(),
				journal.getPublisher().getName(), journal.getCategory().getId(), journal.getCategory().getName() };

		message.setSubject(template.getMessage(MAIL_EVENT_JOURNAL_PUBLISH + MAIL_SUBJECT_SUFFIX, args));
		message.setText(template.getMessage(MAIL_EVENT_JOURNAL_PUBLISH + MAIL_TEXT_SUFFIX, args));

		sendMessage(message);
	}

	/**
	 * Send a message by email for subscribed user informing a set of journals
	 * published in a interval (e.g.: daily).
	 */
	@Override
	public void sendNewJournalsMessage(User user, List<Journal> journals) {
		MessageDTO message = new MessageDTO();
		message.setEmail(user.getEmail());

		Object[] args;

		args = new Object[] { user.getLoginName(), user.getEmail() };

		message.setSubject(template.getMessage(MAIL_EVENT_NEW_JOURNALS + MAIL_SUBJECT_SUFFIX, args));

		StringBuilder text = new StringBuilder();
		text.append(template.getMessage(MAIL_EVENT_NEW_JOURNALS + MAIL_TEXT_SUFFIX, args));
		text.append("\n");

		for (Journal journal : journals) {
			args = new Object[] { user.getLoginName(), user.getEmail(), journal.getId(), journal.getName(),
					journal.getPublisher().getName(), journal.getCategory().getId(), journal.getCategory().getName() };
			text.append(template.getMessage(MAIL_EVENT_NEW_JOURNALS + MAIL_ITEM_SUFFIX, args));
			text.append("\n");
		}

		message.setText(text.toString());

		sendMessage(message);
	}

	/**
	 * Send a message by email asynchronously using JMS.
	 * 
	 * @param message
	 *            content and destination information
	 */
	private void sendMessage(MessageDTO message) {
		LOG.info("Sending message: " + message);
		jmsTemplate.convertAndSend("mail", message);
		LOG.info("Sent message: " + message);
	}

}
