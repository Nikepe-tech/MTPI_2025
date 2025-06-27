/**
 * Reprezentuje pojedynczą transakcję wykonaną przez użytkownika.
 * Zawiera typ operacji, kwotę, znacznik czasu oraz (opcjonalnie) drugiego użytkownika.
 */
package com.bank.model;

import java.time.LocalDateTime;

public class Transaction {
    private String type;
    private double amount;
    private LocalDateTime timestamp;
    private String targetUser;

    /**
     * Tworzy nową transakcję.
     *
     * @param type       Typ transakcji (np. Deposit, Withdraw, Transfer)
     * @param amount     Kwota transakcji
     * @param targetUser Nazwa drugiego użytkownika (jeśli dotyczy, np. w przypadku przelewu)
     */
    public Transaction(String type, double amount, String targetUser) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.targetUser = targetUser;
    }

    /**
     * Zwraca typ transakcji.
     */
    public String getType() {
        return type;
    }

    /**
     * Zwraca kwotę transakcji.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Zwraca datę i godzinę wykonania transakcji.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Zwraca nazwę użytkownika, jeśli dotyczy.
     */
    public String getTargetUser() {
        return targetUser;
    }

    /**
     * Ustawia znacznik czasu dla transakcji.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}


