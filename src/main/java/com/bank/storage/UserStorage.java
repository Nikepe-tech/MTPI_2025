package com.bank.storage;

import com.bank.model.User;
import java.util.concurrent.ConcurrentHashMap;

public class UserStorage {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password));
        return true;
    }

    public static User login(String username, String password) {
        User user = users.get(username);
        return (user != null && user.getPassword().equals(password)) ? user : null;
    }

    public static User getUser(String username) {
        return users.get(username);
    }
}
