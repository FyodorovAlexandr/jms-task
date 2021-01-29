package ru.fyodorov.server.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fyodorov.server.dao.inter.OperationDao;
import ru.fyodorov.server.dao.mapper.OperationMapper;
import ru.fyodorov.server.service.dto.Operation;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OperationDaoJdbcImpl implements OperationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationDaoJdbcImpl.class);
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplateObject;

    @Autowired
    public OperationDaoJdbcImpl(DataSource dataSource, JdbcTemplate jdbcTemplateObject) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = jdbcTemplateObject;
    }

    @Override
    public List<Operation> getAccountOperations(Integer accountNumber) {
        String sql = "select * from operations where account_from=? or account_to=?";
        return jdbcTemplateObject.query(sql,
                new Object[]{accountNumber, accountNumber}, new OperationMapper());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addOperation(Integer accountFrom, Integer accountTo, Double amount) {
        String sql = "insert into operations (account_to, account_from, amount) values (?, ?, ?)";
        jdbcTemplateObject.update(sql, accountTo, accountFrom, amount);
        LOGGER.info("Added operation to db, from={0}, to={1}, amount={2}", accountFrom, accountTo, amount);
    }

}
