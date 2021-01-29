package ru.fyodorov.server.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fyodorov.server.dao.exception.TransferException;
import ru.fyodorov.server.dao.inter.AccountDao;
import ru.fyodorov.server.dao.mapper.AccountMapper;
import ru.fyodorov.server.service.dto.Account;

import javax.sql.DataSource;

@Repository
public class AccountDaoJdbcImpl implements AccountDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDaoJdbcImpl.class);
    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplateObject;

    @Autowired
    public AccountDaoJdbcImpl(DataSource dataSource, JdbcTemplate jdbcTemplateObject) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = jdbcTemplateObject;
    }

    @Override
    public Account getAccount(Integer accountNumber) {
        String sql = "select * from accounts where account_number = ?";
        return jdbcTemplateObject.queryForObject(sql,
                new Object[]{accountNumber}, new AccountMapper());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void decreaseBalance(Integer accountNumber, Double amount) throws TransferException {
        String sql = "select * from accounts where account_number = ?";
        Account currentState = jdbcTemplateObject.queryForObject(sql,
                new Object[]{accountNumber}, new AccountMapper());
        Double newAmount = currentState.getBalance() - amount;
        if (newAmount > 0) {
            String sqlUpdate = "update accounts set balance=? where account_number = ?";
            jdbcTemplateObject.update(sqlUpdate, newAmount, accountNumber);
        } else {
            throw new TransferException("Wrong balance");
        }
        LOGGER.info("Decreased balance, account={0}, amount={1}", accountNumber, amount);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void increaseBalance(Integer accountNumber, Double amount) {
        String sql = "select * from accounts where account_number = ?";
        Account currentState = jdbcTemplateObject.queryForObject(sql,
                new Object[]{accountNumber}, new AccountMapper());
        Double newAmount = currentState.getBalance() + amount;
        String sqlUpdate = "update accounts set balance=? where account_number = ?";
        jdbcTemplateObject.update(sqlUpdate, newAmount, accountNumber);
    }

    public void create(Account account) {
        jdbcTemplateObject.update("INSERT INTO accounts VALUES (?, ?, ?)",
                account.getAccountNumber(), account.getOwner(), account.getBalance());
    }

    public void close(int account_number) {
        jdbcTemplateObject.update("DELETE FROM accounts WHERE account_number=?", account_number);
    }
}
