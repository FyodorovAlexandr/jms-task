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

@Component("accountListener")
public class AccountListener {
    private static final String ACCOUNT_REQUEST_QUEUE = "accountRequest.in";
    private static final String ACCOUNT_RESPONSE_QUEUE = "accountRequest.out";
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountListener.class);
    AccountService accountService;

    @Autowired
    public AccountListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @JmsListener(destination = ACCOUNT_REQUEST_QUEUE)
    @SendTo(ACCOUNT_RESPONSE_QUEUE)
    public String receiveQueueMessage(final Message message) {
        String accountNumber;
        Account account = null;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                accountNumber = textMessage.getText().replace("\"", "");
                account = accountService.getAccountInfo(accountNumber);
            }
        } catch (JMSException e) {
            LOGGER.warn("Error on getting message from " + ACCOUNT_REQUEST_QUEUE, e);
        }
        Gson gson = new Gson();
        return gson.toJson(account);
    }
}
