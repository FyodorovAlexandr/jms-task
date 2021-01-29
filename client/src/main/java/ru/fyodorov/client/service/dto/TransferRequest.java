package ru.fyodorov.client.service.dto;

public class TransferRequest {
    Integer accountFrom;
    Integer accountTo;
    Double amount;

    public TransferRequest(Integer accountFrom, Integer accountTo, Double amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public TransferRequest() {
    }

    public Integer getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Integer getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Integer accountTo) {
        this.accountTo = accountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
