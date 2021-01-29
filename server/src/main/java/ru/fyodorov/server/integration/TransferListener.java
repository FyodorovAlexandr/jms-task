package ru.fyodorov.server.integration;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.fyodorov.server.dao.AccountDaoJdbcImpl;
import ru.fyodorov.server.service.AccountService;
import ru.fyodorov.server.service.dto.TransferRequest;
import ru.fyodorov.server.service.dto.TransferResult;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component("transferListener")
public class TransferListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDaoJdbcImpl.class);
    private static final String TRANSFER_REQUEST_QUEUE = "transferRequest.in";
    private static final String TRANSFER_RESPONSE_QUEUE = "transferRequest.out";

    private AccountService accountService;

    @Autowired
    public TransferListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @JmsListener(destination = TRANSFER_REQUEST_QUEUE)
    @SendTo(TRANSFER_RESPONSE_QUEUE)
    public String receiveQueueMessage(final Message message) {
        TransferRequest transferRequest;
        TransferResult result = null;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                transferRequest = new Gson().fromJson(
                        textMessage.getText(),
                        TransferRequest.class);
                result = accountService.makeTransfer(transferRequest);
            }
        } catch (JMSException e) {
            LOGGER.warn("Error on getting message from " + TRANSFER_REQUEST_QUEUE, e);
        }
        return new Gson().toJson(result);
    }
}
