package com.bank.model;

import java.time.LocalDateTime;

public class Transaction {
    private String type;
    private double amount;
    private LocalDateTime timestamp;
    private String targetUser;

    public Transaction(String type, double amount, String targetUser) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.targetUser = targetUser;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}


