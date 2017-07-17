package com.crossover.trial.journals.service;

import java.util.List;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;

/**
 * This class implements required logic to send notification messages as
 * requested by trial requirements.
 * 
 * @author marciofranca
 *
 */
public interface MessageService {

	/**
	 * Send a message by email for subscribed user when a new journal is published.
	 */
	void sendJournalPublishMessage(User user, Journal journal);

	/**
	 * Send a message by email for subscribed user informing a set of journals
	 * published in a interval (e.g.: daily).
	 */
	void sendNewJournalsMessage(User user, List<Journal> journal);
}