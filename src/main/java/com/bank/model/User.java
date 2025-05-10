package com.bank.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private double balance;
    private List<String> history;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.history = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public List<String> getHistory() { return history; }

    public void deposit(double amount) {
        this.balance += amount;
        history.add("Doładowanie: +" + amount);
    }
}