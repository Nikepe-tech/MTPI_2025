package com.bank.storage;

import com.bank.model.User;

import javax.json.*;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserStorage {
    // Путь к файлу с пользователями
    private static final String FILE_NAME = "C:/Users/nikit/IdeaProjects/Bank_EE/users.json";

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
        JsonObjectBuilder rootBuilder = Json.createObjectBuilder();

        for (Map.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();

            // Строим массив истории операций
            JsonArrayBuilder historyBuilder = Json.createArrayBuilder();
            for (String h : user.getHistory()) {
                historyBuilder.add(h);
            }

            // Собираем JSON для каждого пользователя
            JsonObject userJson = Json.createObjectBuilder()
                    .add("username", user.getUsername())
                    .add("password", user.getPassword())
                    .add("balance", user.getBalance())
                    .add("history", historyBuilder)
                    .build();

            // Добавляем в корневой объект по ключу username
            rootBuilder.add(entry.getKey(), userJson);
        }

        // Записываем объект в файл
        try (Writer writer = new FileWriter(FILE_NAME)) {
            JsonWriter jsonWriter = Json.createWriter(writer);
            jsonWriter.writeObject(rootBuilder.build());
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

            fromUser.withdraw(amount);
            toUser.deposit(amount);

            fromUser.getHistory().add("Transfer to " + toUsername + ": -" + amount);
            toUser.getHistory().add("Transfer from " + fromUsername + ": +" + amount);

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

                arrayBuilder.add(userJson);
            }

            jsonWriter.writeArray(arrayBuilder.build());

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

                loadedUsers.put(username, user);
            }
        } catch (Exception e) {
            // Логируем ошибку и возвращаем пустой список, чтобы не падать
            e.printStackTrace();
        }
        return loadedUsers;
    }
}
