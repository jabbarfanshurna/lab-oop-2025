package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kanbanlitegradle.model.*;

import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button backButton;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username dan password tidak boleh kosong.");
            return;
        }

        try {
            List<User> users = UserDataStore.loadUsers();
            AppContext.setUserList(users);

            User loggedInUser = null;
            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(username) &&
                        user.getPassword().equals(password)) {
                    loggedInUser = user;
                    break;
                }
            }

            if (loggedInUser == null) {
                showAlert("Login gagal. Username atau password salah.");
                return;
            }

            AppContext.setCurrentUser(loggedInUser);

            Parent dashboardRoot;
            if (loggedInUser instanceof Manager) {
                dashboardRoot = FXMLLoader.load(getClass().getResource("/DashboardManager.fxml"));
            } else {
                dashboardRoot = FXMLLoader.load(getClass().getResource("/DashboardMember.fxml"));
            }

            dashboardRoot.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Terjadi kesalahan saat memuat dashboard.");
        }
    }

    @FXML
    private void goBack(javafx.event.ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/StartMenu.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}