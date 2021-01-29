package ru.fyodorov.server.integration;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.fyodorov.server.service.AccountService;
import ru.fyodorov.server.service.dto.Operation;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.List;

@Component("operationsListener")
public class OperationsListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountListener.class);
    private static final String OPERATIONS_REQUEST_QUEUE = "operationsRequest.in";
    private static final String OPERATIONS_RESPONSE_QUEUE = "operationsRequest.out";

    private AccountService accountService;

    @Autowired
    public OperationsListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @JmsListener(destination = OPERATIONS_REQUEST_QUEUE)
    @SendTo(OPERATIONS_RESPONSE_QUEUE)
    public String receiveQueueMessage(final Message message) {
        String accountNumber;
        List<Operation> operations = null;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                accountNumber = textMessage.getText().replace("\"", "");
                operations = accountService.getAccountOperations(accountNumber);
            }
        } catch (JMSException e) {
            LOGGER.warn("Error on getting message from " + OPERATIONS_REQUEST_QUEUE, e);
        }
        Gson gson = new Gson();
        return gson.toJson(operations);
    }
}

