package kanbanlitegradle.model;

import java.util.ArrayList;
import java.util.List;

public class UserRegistry {

    private static List<User> userList = new ArrayList<>();

    public static void addUser(User user) {
        if (findUserByName(user.getUsername()) == null) {
            userList.add(user);
        }
    }

    public static User findUserByName(String name) {
        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getAllUsers() {
        return userList;
    }

    public static void clearUsers() {
        userList.clear();
    }
}