package ru.fyodorov.client.service.dto;

public class TransferResult {
    private int status;
    private double amount;
    private String accountTarget;

    public TransferResult(int status, double amount, String accountTarget) {
        this.status = status;
        this.amount = amount;
        this.accountTarget = accountTarget;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccountTarget() {
        return accountTarget;
    }

    public void setAccountTarget(String accountTarget) {
        this.accountTarget = accountTarget;
    }
}
