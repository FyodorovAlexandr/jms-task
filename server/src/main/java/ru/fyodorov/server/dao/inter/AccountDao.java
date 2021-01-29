package ru.fyodorov.server.dao.inter;

import org.springframework.stereotype.Repository;
import ru.fyodorov.server.dao.exception.TransferException;
import ru.fyodorov.server.service.dto.Account;

@Repository
public interface AccountDao {
    Account getAccount(Integer accountNumber);

    public void increaseBalance(Integer accountNumber, Double amount);

    public void decreaseBalance(Integer accountNumber, Double amount) throws TransferException;

    public void create(Account account);

    public void close(int id);

//    public List<Account> findAll();
}
