package ru.fyodorov.server.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.fyodorov.server.service.dto.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<Account> {
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setAccountNumber(rs.getInt("account_number"));
        account.setOwner(rs.getString("owner"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}
