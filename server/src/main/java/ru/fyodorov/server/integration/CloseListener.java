package ru.fyodorov.server.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.fyodorov.server.service.AccountService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component("closeListener")
public class CloseListener {
    private static final String CLOSE_REQUEST_QUEUE = "closeRequest.in";
    private static final String CLOSE_RESPONSE_QUEUE = "closeRequest.out";
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseListener.class);
    AccountService accountService;

    @Autowired
    public CloseListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @JmsListener(destination = CLOSE_REQUEST_QUEUE)
    @SendTo(CLOSE_RESPONSE_QUEUE)
    public void receiveQueueMessage(final Message message) {
        String accountNumber;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                accountNumber = textMessage.getText().replace("\"", "");
                accountService.getClose(Integer.valueOf(accountNumber));
            }
        } catch (JMSException e) {
            LOGGER.warn("Error on getting message from " + CLOSE_REQUEST_QUEUE, e);
        }
    }
}
