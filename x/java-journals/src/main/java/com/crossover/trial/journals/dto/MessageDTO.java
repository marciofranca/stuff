package com.crossover.trial.journals.dto;

import java.util.Calendar;

/**
 * DTO created to transfer message information in mail notifications.
 * 
 * @author marciofranca
 *
 */
public class MessageDTO {

	private long date;
	private String email;

	private String subject;
	private String text;

	public MessageDTO() {
		this.date = Calendar.getInstance().getTimeInMillis();
	}

	public MessageDTO(String subject) {
		this();
		this.subject = subject;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("Message [date=%d, email=%s, subject=%s, text=%s]", getDate(), getEmail(), getSubject(),
				getText());
	}

}
