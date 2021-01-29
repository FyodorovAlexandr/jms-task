package ru.fyodorov.client.integration.jms;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import java.util.UUID;

@Component
public class JmsSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(JmsSender.class);

    @Autowired
    private ConnectionFactory connectionFactory;
    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void init() {
        this.jmsTemplate = new JmsTemplate(connectionFactory);
    }

    public void sendMessage(String queueName, Object sending, UUID correlationId) {
        Gson gson = new Gson();
        String messageText = gson.toJson(sending);
        try {
            jmsTemplate.send(queueName, session -> {
                Message message = session.createTextMessage(messageText);
                message.setJMSCorrelationID(correlationId.toString());
                return message;
            });
        } catch (JmsException e) {
            LOGGER.warn("Error on sending message" + messageText, e);
        }
        LOGGER.debug("Sent: %s Queue: %s", messageText, queueName);
    }
}
