package ru.fyodorov.client.integration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fyodorov.client.integration.jms.JmsReceiver;
import ru.fyodorov.client.integration.jms.JmsSender;
import ru.fyodorov.client.service.dto.Account;
import ru.fyodorov.client.service.dto.Operation;
import ru.fyodorov.client.service.dto.TransferRequest;
import ru.fyodorov.client.service.dto.TransferResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DataProvider {
    private static final String ACCOUNT_REQUEST_QUEUE="accountRequest.in";
    private static final String ACCOUNT_RESPONSE_QUEUE="accountRequest.out";
    private static final String OPERATIONS_REQUEST_QUEUE = "operationsRequest.in";
    private static final String OPERATIONS_RESPONSE_QUEUE = "operationsRequest.out";
    private static final String TRANSFER_REQUEST_QUEUE = "transferRequest.in";
    private static final String TRANSFER_RESPONSE_QUEUE = "transferRequest.out";
    private static final String CREATE_REQUEST_QUEUE = "createRequest.in";
    private static final String CREATE_RESPONSE_QUEUE = "createRequest.out";
    private static final String CLOSE_REQUEST_QUEUE = "closeRequest.in";
    private static final String CLOSE_RESPONSE_QUEUE = "closeRequest.out";

    @Autowired
    private JmsReceiver receiver;
    @Autowired
    private JmsSender sender;


    public Account getAccountInfo(String accountNumber){
        UUID correlationID = UUID.randomUUID();
        sender.sendMessage(ACCOUNT_REQUEST_QUEUE, accountNumber, correlationID);
        return new Gson().fromJson(
                receiver.receiveMessage(ACCOUNT_RESPONSE_QUEUE, correlationID),
                Account.class);
    }

    public List<Operation> getOperationsList(String accountNumber) {
        UUID correlationID = UUID.randomUUID();
        sender.sendMessage(OPERATIONS_REQUEST_QUEUE, accountNumber, correlationID);
        Type listType = new TypeToken<ArrayList<Operation>>() {
        }.getType();
        return new Gson().fromJson(
                receiver.receiveMessage(OPERATIONS_RESPONSE_QUEUE, correlationID),
                listType);
    }

    public TransferResult makeTransfer(TransferRequest request) {
        UUID correlationID = UUID.randomUUID();
        sender.sendMessage(TRANSFER_REQUEST_QUEUE, request, correlationID);
        return new Gson().fromJson(
                receiver.receiveMessage(TRANSFER_RESPONSE_QUEUE, correlationID),
                TransferResult.class);
    }

    public Account createAccount(Account account) {
        UUID correlationID = UUID.randomUUID();
        sender.sendMessage(CREATE_REQUEST_QUEUE, account, correlationID);
        return new Gson().fromJson(
                receiver.receiveMessage(CREATE_RESPONSE_QUEUE, correlationID),
                Account.class);
    }

    public String closeAccount(int id) {
        UUID correlationID = UUID.randomUUID();
        sender.sendMessage(CLOSE_REQUEST_QUEUE, id, correlationID);
        return new Gson().fromJson(
                receiver.receiveMessage(CLOSE_RESPONSE_QUEUE, correlationID),
                String.class);
    }
}
