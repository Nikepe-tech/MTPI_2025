package com.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private double balance;      // Баланс счета
    private List<String> history; // История операций

    // Конструктор — создает пользователя с нулевым балансом и пустой историей
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.history = new ArrayList<>();
    }

    // Метод для снятия денег
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            history.add("Withdrawal: -" + amount); // Добавляем в историю операцию снятия
            return true;
        }
        return false; // Если сумма неверная или недостаточно средств
    }

    // Метод для внесения денег
    public void deposit(double amount) {
        this.balance += amount;
        history.add("Deposit: +" + amount); // Добавляем в историю операцию пополнения
    }

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public List<String> getHistory() { return history; }
    public void setBalance(double balance) { this.balance = balance; }
}
