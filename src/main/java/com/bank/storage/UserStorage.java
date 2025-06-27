/**
 * Klasa odpowiedzialna za przechowywanie i zarządzanie użytkownikami systemu.
 * Obsługuje logowanie, rejestrację, operacje finansowe oraz zapis/odczyt do pliku JSON.
 */

package com.bank.storage;

import com.bank.model.Transaction;
import com.bank.model.User;

import javax.json.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserStorage {
    /**
     * Ścieżka do pliku JSON, w którym przechowywani są użytkownicy.
     */
    private final String FILE_NAME = "C:/Users/nikit/IdeaProjects/BankAppData/users.json";

    /**
     * Mapa wszystkich użytkowników w systemie, z kluczem jako nazwa użytkownika.
     */
    private final ConcurrentHashMap<String, User> users;

    /**
     * Konstruktor wczytujący użytkowników z pliku JSON do pamięci.
     */
    public UserStorage() {
        users = loadUsers();
    }

    /**
     * Rejestruje nowego użytkownika, jeśli nie istnieje.
     */
    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        User user = new User(username, password);
        users.put(username, user);
        saveUsers();
        return true;
    }

    /**
     * Loguje użytkownika na podstawie podanych danych.
     */
    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }

    /**
     * Zwraca obiekt użytkownika na podstawie nazwy.
     */
    public User getUser(String username) {
        return users.get(username);
    }

    /**
     * Zapisuje wszystkich użytkowników do pliku JSON.
     */
    public void saveUsers() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (User user : users.values()) {
            JsonObjectBuilder userJson = Json.createObjectBuilder()
                    .add("username", user.getUsername())
                    .add("password", user.getPassword())
                    .add("balance", user.getBalance());

            JsonArrayBuilder historyArray = Json.createArrayBuilder();
            for (String h : user.getHistory()) {
                historyArray.add(h);
            }

            userJson.add("history", historyArray);
            arrayBuilder.add(userJson);
        }

        JsonObject root = Json.createObjectBuilder()
                .add("users", arrayBuilder)
                .build();

        try (JsonWriter writer = Json.createWriter(new FileWriter(FILE_NAME))) {
            writer.writeObject(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Przesyła środki między użytkownikami jako transfer z historią.
     */
    public boolean transfer(String fromUsername, String toUsername, double amount) {
        if (amount <= 0) return false;

        User fromUser = users.get(fromUsername);
        User toUser = users.get(toUsername);

        if (fromUser == null || toUser == null) return false;

        synchronized (users) {
            if (fromUser.getBalance() < amount) return false;

            fromUser.setBalance(fromUser.getBalance() - amount);
            toUser.setBalance(toUser.getBalance() + amount);

            fromUser.getHistory().add("Transfer to " + toUsername + ": -" + amount);
            toUser.getHistory().add("Transfer from " + fromUsername + ": +" + amount);

            fromUser.addTransaction(new Transaction("TransferOut", amount, toUsername));
            toUser.addTransaction(new Transaction("TransferIn", amount, fromUsername));

            saveToFile();
            return true;
        }
    }


    /**
     * Dodaje środki do salda użytkownika i zapisuje transakcję.
     */
    public boolean deposit(String username, double amount) {
        if (amount <= 0) return false;

        User user = users.get(username);
        if (user == null) return false;

        synchronized (users) {
            double newBalance = user.getBalance() + amount;
            user.setBalance(newBalance);

            user.getHistory().add("Deposit: +" + amount);
            user.addTransaction(new Transaction("Deposit", amount, null));

            saveToFile();
            return true;
        }
    }


    /**
     * Odejmuje środki z salda użytkownika, jeśli wystarczające.
     */
    public boolean withdraw(String username, double amount) {
        if (amount <= 0) return false;

        User user = users.get(username);
        if (user == null || user.getBalance() < amount) return false;

        synchronized (users) {
            double newBalance = user.getBalance() - amount;
            user.setBalance(newBalance);

            user.getHistory().add("Withdrawal: -" + amount);
            user.addTransaction(new Transaction("Withdrawal", amount, null));

            saveToFile();
            return true;
        }
    }


    /**
     * Prywatna metoda zapisująca dane użytkowników do pliku JSON.
     */
    public void saveToFile() {
        try (FileWriter fw = new FileWriter(FILE_NAME);
             JsonWriter jsonWriter = Json.createWriter(fw)) {

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (User user : users.values()) {
                JsonObjectBuilder userJson = Json.createObjectBuilder()
                        .add("username", user.getUsername())
                        .add("password", user.getPassword())
                        .add("balance", user.getBalance());

                JsonArrayBuilder historyArray = Json.createArrayBuilder();
                for (String entry : user.getHistory()) {
                    historyArray.add(entry);
                }
                userJson.add("history", historyArray);

                JsonArrayBuilder transactionsArray = Json.createArrayBuilder();
                for (Transaction tx : user.getTransactions()) {
                    JsonObjectBuilder txJson = Json.createObjectBuilder()
                            .add("type", tx.getType())
                            .add("amount", tx.getAmount())
                            .add("timestamp", tx.getTimestamp().toString());

                    if (tx.getTargetUser() != null) {
                        txJson.add("targetUser", tx.getTargetUser());
                    }

                    transactionsArray.add(txJson);
                }
                userJson.add("transactions", transactionsArray);


                arrayBuilder.add(userJson);
            }

            JsonObject rootObject = Json.createObjectBuilder()
                    .add("users", arrayBuilder)
                    .build();
            jsonWriter.writeObject(rootObject);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje użytkowników z pliku JSON i odtwarza ich stan.
     */
    public ConcurrentHashMap<String, User> loadUsers() {
        ConcurrentHashMap<String, User> loadedUsers = new ConcurrentHashMap<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write("{\"users\":[]}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return loadedUsers;
        }

        try (Reader reader = new FileReader(file)) {
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject rootObject = jsonReader.readObject();
            JsonArray usersArray = rootObject.getJsonArray("users");
            if (usersArray == null) {
                return loadedUsers;
            }

            for (JsonValue val : usersArray) {
                JsonObject userJson = val.asJsonObject();

                String username = userJson.getString("username");
                String password = userJson.getString("password");
                double balance = userJson.getJsonNumber("balance").doubleValue();

                User user = new User(username, password);
                user.setBalance(balance);

                JsonArray historyArray = userJson.getJsonArray("history");
                for (JsonValue historyVal : historyArray) {
                    user.getHistory().add(((JsonString) historyVal).getString());
                }

                JsonArray transactionsArray = userJson.getJsonArray("transactions");
                if (transactionsArray != null) {
                    for (JsonValue txVal : transactionsArray) {
                        JsonObject txJson = txVal.asJsonObject();

                        String type = txJson.getString("type");
                        double amount = txJson.getJsonNumber("amount").doubleValue();
                        String timestampStr = txJson.getString("timestamp");
                        java.time.LocalDateTime timestamp = java.time.LocalDateTime.parse(timestampStr);
                        String targetUser = txJson.containsKey("targetUser") ? txJson.getString("targetUser") : null;

                        com.bank.model.Transaction tx = new com.bank.model.Transaction(type, amount, targetUser);
                        tx.setTimestamp(timestamp);

                        user.getTransactions().add(tx);
                    }
                }


                loadedUsers.put(username, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadedUsers;
    }
}
