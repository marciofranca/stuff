package com.crossover.trial.journals.config;

import javax.jms.ConnectionFactory;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.util.backoff.ExponentialBackOff;

import com.crossover.trial.journals.service.MessageServiceImpl;

/**
 * Configuration needed for JMS communication and processing. More configuration
 * can be done in application.properties (spring.jms.*).
 * 
 * @author marciofranca
 *
 */
@EnableJms
@Configuration
public class JmsConfig {
	private static final Logger LOG = Logger.getLogger(MessageServiceImpl.class);

	/**
	 * JMS container configuration, which can define concurrency, error handling,
	 * retry and communication.
	 */
	@Bean
	public JmsListenerContainerFactory containerFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		// Recovery policy can be increased at each retry.
		// Parameters can be defined as required by project
		ExponentialBackOff backOff = new ExponentialBackOff();
		backOff.setInitialInterval(10000L);
		backOff.setMultiplier(2.0);
		backOff.setMaxElapsedTime(24 * 60 * 60 * 1000L);
		backOff.setMaxInterval(10 * 60 * 1000L);
		factory.setBackOff(backOff);

		// Error handling can be implemented according with requirements for this
		// project.
		factory.setErrorHandler(error -> LOG.warn("Error Handler", error));
		factory.setMessageConverter(converter());

		configurer.configure(factory, connectionFactory);
		return factory;
	}

	/**
	 * As MessageDTO is a object, its communication can be done using a converter.
	 */
	@Bean
	public MessageConverter converter() {
		return new MessageDTOConverter();
	}

	/**
	 * JmsTemplate configured be used for client to publish new messages.
	 */
	@Bean
	public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
		JmsTemplate result = new JmsTemplate(connectionFactory);
		result.setMessageConverter(converter());
		return result;
	}
}
