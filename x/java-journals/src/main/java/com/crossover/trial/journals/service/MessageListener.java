package com.crossover.trial.journals.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.crossover.trial.journals.dto.MessageDTO;

@Component
public class MessageListener {
	private static final Logger LOG = Logger.getLogger(MessageServiceImpl.class);

	private MailSender sender;

	@Autowired
	public MessageListener(MailSender sender) {
		super();
		this.sender = sender;
	}

	@JmsListener(destination = "mail", containerFactory = "containerFactory")
	public void receiveMessage(MessageDTO message) {
		COUNT.incrementAndGet();
		LOG.info("Received message: " + message);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setText(message.getText());
		mail.setTo(message.getEmail());
		mail.setSubject(message.getSubject());

		sender.send(mail);
		LOG.info("Mail sent: " + message);
		COUNT.decrementAndGet();
	}

	// Hack for unit tests :(
	public static final AtomicInteger COUNT = new AtomicInteger();
}
