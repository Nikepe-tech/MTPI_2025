package com.bank.storage;

import com.bank.model.Transaction;
import com.bank.model.User;

import javax.json.*;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserStorage {
    // Путь к файлу с пользователями
    private static final String FILE_NAME = "C:/Users/nikit/IdeaProjects/BankAppData/users.json";

    // Все пользователи хранятся тут, загружаются из файла при старте
    private static ConcurrentHashMap<String, User> users = loadUsers();

    // Регистрируем нового юзера: если имя занято — отказ, иначе создаём, добавляем и сохраняем
    public static boolean register(String username, String password) {
        if (users.containsKey(username)) return false;  // пользователь уже есть
        User user = new User(username, password);
        users.put(username, user);
        saveUsers();  // обновляем файл
        return true;
    }

    // Логиним пользователя: берём из памяти, проверяем пароль
    public static User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }

    // Получаем пользователя из памяти по имени
    public static User getUser(String username) {
        return users.get(username);
    }

    // Сохраняем всех пользователей в файл в виде JSON-объекта с ключами — username
    // Формат: { "user1": {...}, "user2": {...} }
    public static void saveUsers() {
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

    // Перевод денег между пользователями
    // Проверяем сумму, что оба пользователя есть, что у отправителя достаточно денег
    // Меняем балансы, добавляем запись в историю, сохраняем изменения в файл
    // Синхронизируем доступ, чтобы избежать проблем при параллельных переводах
    public static boolean transfer(String fromUsername, String toUsername, double amount) {
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

            saveToFile();  // сохраняем изменения
            return true;
        }
    }

    // Сохраняем всех пользователей в файл в формате JSON-массива
    // Формат: [ {...user1...}, {...user2...} ]
    public static void saveToFile() {
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

    // Загружаем пользователей из файла
    // Ожидается JSON-массив, где каждый элемент — объект пользователя с username, password, balance и history
    // Если файла нет — возвращаем пустую коллекцию
    public static ConcurrentHashMap<String, User> loadUsers() {
        ConcurrentHashMap<String, User> loadedUsers = new ConcurrentHashMap<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            // Если файла нет — создаём пустой файл с валидным JSON
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
                        tx.setTimestamp(timestamp);  // не забудь добавить setTimestamp() в Transaction.java

                        user.getTransactions().add(tx);
                    }
                }



                loadedUsers.put(username, user);
            }
        } catch (Exception e) {
            // Логируем ошибку и возвращаем пустой список, чтобы не падать
            e.printStackTrace();
        }
        return loadedUsers;
    }
}
