/**
 * Reprezentuje użytkownika systemu bankowego.
 * Przechowuje dane logowania, saldo, historię oraz transakcje.
 */
package com.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    private List<String> history;

    /**
     * Tworzy nowego użytkownika z początkowym saldem 0.
     *
     * @param username Nazwa użytkownika
     * @param password Hasło użytkownika
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.history = new ArrayList<>();
    }

    /**
     * Dodaje nową transakcję do listy użytkownika.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Zwraca nazwę użytkownika.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Zwraca hasło użytkownika.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Zwraca aktualne saldo użytkownika.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Zwraca szczegółową listę transakcji użytkownika.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Zwraca historię operacji użytkownika jako tekst.
     */
    public List<String> getHistory() {
        return history;
    }

    /**
     * Ustawia nowe saldo użytkownika.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
