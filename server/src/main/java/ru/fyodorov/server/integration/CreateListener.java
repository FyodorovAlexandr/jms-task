package ru.fyodorov.server.integration;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.fyodorov.server.service.AccountService;
import ru.fyodorov.server.service.dto.Account;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component("createListener")
public class CreateListener {
    private static final String CREATE_REQUEST_QUEUE = "createRequest.in";
    private static final String CREATE_RESPONSE_QUEUE = "createRequest.out";
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateListener.class);
    AccountService accountService;

    @Autowired
    public CreateListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @JmsListener(destination = CREATE_REQUEST_QUEUE)
    @SendTo(CREATE_RESPONSE_QUEUE)
    public void receiveQueueMessage(final Message message) {
        Account accountNumber;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                accountNumber = new Gson().fromJson(
                        textMessage.getText(), Account.class);
                accountService.getCreate(accountNumber);
            }
        } catch (JMSException e) {
            LOGGER.warn("Error on getting message from " + CREATE_REQUEST_QUEUE, e);
        }
    }
}
