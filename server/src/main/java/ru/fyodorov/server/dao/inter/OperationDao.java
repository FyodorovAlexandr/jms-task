package ru.fyodorov.server.dao.inter;

import ru.fyodorov.server.service.dto.Operation;

import java.util.List;

public interface OperationDao {
    List<Operation> getAccountOperations(Integer accountNumber);

    void addOperation(Integer accountFrom, Integer accountTo, Double amount);
}
