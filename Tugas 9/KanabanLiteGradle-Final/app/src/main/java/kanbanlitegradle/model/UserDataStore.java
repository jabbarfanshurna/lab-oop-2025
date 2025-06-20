package kanbanlitegradle.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserDataStore {
    private static final String FILE_PATH = "src/main/resources/data/users.json";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserAdapter())
            .setPrettyPrinting()
            .create();

    public static void initializeUsers() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                createDefaultUserFile();
            } else {
                validateExistingUserFile();
            }
        } catch (Exception e) {
            System.err.println("Error during user initialization: " + e.getMessage());
            createDefaultUserFile();
        }
    }

    private static void createDefaultUserFile() {
        System.out.println("Creating new users.json with default users...");
        List<User> defaultUsers = new ArrayList<>();
        defaultUsers.add(new Manager("manager", "manager123", "manager"));
        saveUsers(defaultUsers);
    }

    private static void validateExistingUserFile() throws IOException {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type userListType = new TypeToken<List<User>>() {
            }.getType();
            List<User> existingUsers = gson.fromJson(reader, userListType);

            if (existingUsers == null || existingUsers.isEmpty()) {
                System.out.println("File is empty, creating default users...");
                createDefaultUserFile();
            } else {

                for (User user : existingUsers) {
                    if (user.getRole() == null) {
                        throw new IOException("Corrupted user data: missing role");
                    }
                }
                System.out.println("User file validation successful");
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON format, recreating file...");
            createDefaultUserFile();
        }
    }

    public static void saveUsers(List<User> users) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    public static List<User> loadUsers() {
        if (!Files.exists(Paths.get(FILE_PATH))) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(FILE_PATH)) {
            Type userListType = new TypeToken<List<User>>() {
            }.getType();
            List<User> users = gson.fromJson(reader, userListType);

            if (users != null) {
                users.removeIf(user -> user.getRole() == null);
                return users;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean usernameExists(String username) {
        List<User> users = loadUsers();
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public static void addUser(User newUser) {
        List<User> users = loadUsers();
        users.add(newUser);
        saveUsers(users);
    }
}