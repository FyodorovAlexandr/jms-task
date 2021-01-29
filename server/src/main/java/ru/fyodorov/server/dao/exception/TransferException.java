package ru.fyodorov.server.dao.exception;

import java.sql.SQLException;

public class TransferException extends SQLException {
    public TransferException(String reason) {
        super(reason);
    }
}
