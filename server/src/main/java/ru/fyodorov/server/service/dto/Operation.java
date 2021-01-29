package ru.fyodorov.server.service.dto;

public class Operation {
    private String accountFrom;
    private String accountTo;
    private Double summ;

    public Operation(String accountFrom, String accountTo, Double summ) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.summ = summ;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }
}
