package ru.fyodorov.server.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.fyodorov.server.service.dto.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OperationMapper implements RowMapper<Operation> {
    public Operation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Operation(
                String.valueOf(rs.getInt("account_from")),
                String.valueOf(rs.getInt("account_to")),
                rs.getDouble("amount")
        );
    }
}
