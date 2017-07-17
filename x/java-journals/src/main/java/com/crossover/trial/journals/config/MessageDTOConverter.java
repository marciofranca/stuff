package com.crossover.trial.journals.config;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import com.crossover.trial.journals.dto.MessageDTO;

/**
 * Converter created to support MessageDTO communication over JMS.
 * 
 * @author marciofranca
 *
 */
public class MessageDTOConverter implements org.springframework.jms.support.converter.MessageConverter {
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_SUBJECT = "subject";
	private static final String FIELD_TEXT = "text";
	private static final String FIELD_DATE = "date";

	@Override
	public Message toMessage(Object object, Session session) throws JMSException {
		MapMessage map = session.createMapMessage();
		if (object instanceof MessageDTO) {
			MessageDTO dto = (MessageDTO) object;
			map.setString(FIELD_EMAIL, dto.getEmail());
			map.setString(FIELD_SUBJECT, dto.getSubject());
			map.setString(FIELD_TEXT, dto.getText());
			map.setLong(FIELD_DATE, dto.getDate());
		}
		return map;
	}

	@Override
	public Object fromMessage(Message message) throws JMSException {
		MessageDTO dto = new MessageDTO();
		if (message instanceof MapMessage) {
			MapMessage map = (MapMessage) message;
			dto.setEmail(map.getString(FIELD_EMAIL));
			dto.setSubject(map.getString(FIELD_SUBJECT));
			dto.setText(map.getString(FIELD_TEXT));
			dto.setDate(map.getLong(FIELD_DATE));
		}
		return dto;
	}

}
