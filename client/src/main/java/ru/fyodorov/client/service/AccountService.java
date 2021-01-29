package ru.fyodorov.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fyodorov.client.integration.DataProvider;
import ru.fyodorov.client.service.dto.Account;
import ru.fyodorov.client.service.dto.Operation;
import ru.fyodorov.client.service.dto.TransferRequest;
import ru.fyodorov.client.service.dto.TransferResult;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    DataProvider dataProvider;

    public Account getAccountInfo(String accountNumber){
        return dataProvider.getAccountInfo(accountNumber);
    }

    public List<Operation> getOperations(String accountNumber) {
        return dataProvider.getOperationsList(accountNumber);
    }

    public TransferResult makeTransfer(String accountNumber, String accountTarget, Double amount){
        TransferRequest request = new TransferRequest(Integer.valueOf(accountNumber), Integer.valueOf(accountTarget), amount);
        return dataProvider.makeTransfer(request);
    }

    public Account createAccount(Account account) {
        return dataProvider.createAccount(account);
    }

    public String closeAccount(int id) {
        return dataProvider.closeAccount(id);
    }
}
