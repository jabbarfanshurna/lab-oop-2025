package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kanbanlitegradle.model.*;

import java.util.List;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Manager", "Member");
        roleComboBox.setValue("Member");

        registerButton.setOnAction(e -> handleRegister());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Input tidak boleh kosong!");
            return;
        }

        if (password.length() < 4) {
            showAlert("Password minimal 4 karakter.");
            return;
        }

        List<User> users = UserDataStore.loadUsers();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                showAlert("Username sudah terdaftar.");
                return;
            }
        }

        User newUser;
        if ("Manager".equals(selectedRole)) {
            newUser = new Manager(username, password, "Manager");
        } else {
            newUser = new Member(username, password, "Member");
        }

        users.add(newUser);
        UserDataStore.saveUsers(users);

        showAlert("Registrasi berhasil!");
        clearForm();
    }

    @FXML
    private void handleBack(javafx.event.ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/StartMenu.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue("Member");
    }
}