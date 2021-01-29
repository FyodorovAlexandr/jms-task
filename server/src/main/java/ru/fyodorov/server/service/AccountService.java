package ru.fyodorov.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fyodorov.server.dao.exception.TransferException;
import ru.fyodorov.server.dao.inter.AccountDao;
import ru.fyodorov.server.dao.inter.OperationDao;
import ru.fyodorov.server.service.dto.Account;
import ru.fyodorov.server.service.dto.Operation;
import ru.fyodorov.server.service.dto.TransferRequest;
import ru.fyodorov.server.service.dto.TransferResult;

import java.util.List;

@Service
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private AccountDao accountDao;
    private OperationDao operationDao;

    @Autowired
    public AccountService(AccountDao accountDao, OperationDao operationDao) {
        this.accountDao = accountDao;
        this.operationDao = operationDao;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Account getAccountInfo(String accountNumber) {
        return accountDao.getAccount(Integer.valueOf(accountNumber));
    }

    public void getCreate(Account account) {
        accountDao.create(account);
    }

    public void getClose(int id) {
        accountDao.close(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Operation> getAccountOperations(String accountNumber) {
        return operationDao.getAccountOperations(Integer.valueOf(accountNumber));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public TransferResult makeTransfer(TransferRequest request) {
        try {
            accountDao.decreaseBalance(request.getAccountFrom(), request.getAmount());
            accountDao.increaseBalance(request.getAccountTo(), request.getAmount());
            operationDao.addOperation(request.getAccountFrom(), request.getAccountTo(), request.getAmount());
            return new TransferResult(0, request.getAmount(), String.valueOf(request.getAccountTo()));
        } catch (RuntimeException e) {
            LOGGER.warn("SQLException when transferring money, account " + request.getAccountFrom(), e);
            return new TransferResult(1, request.getAmount(), String.valueOf(request.getAccountTo()));
        } catch (TransferException e) {
            LOGGER.warn("Incorrect amount when transferring money, account " + request.getAccountFrom(), e);
            return new TransferResult(1, request.getAmount(), String.valueOf(request.getAccountTo()));
        }
    }
}

