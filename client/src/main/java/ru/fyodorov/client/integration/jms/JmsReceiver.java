package ru.fyodorov.client.integration.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.UUID;

@Component
public class JmsReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(JmsReceiver.class);

    @Autowired
    private ConnectionFactory connectionFactory;
    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void init() {
        this.jmsTemplate = new JmsTemplate(connectionFactory);
    }

    public String receiveMessage(String queueName, UUID correlationId) {
        Message message = jmsTemplate.receiveSelected(queueName, "JMSCorrelationID='" + correlationId + "'");
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            LOGGER.debug("Received: {0}. Queue: {1}.", text, queueName);
            return text;
        } catch (JMSException e) {
            LOGGER.warn("Error on receiving message", e);
        }
        return null;
    }
}
