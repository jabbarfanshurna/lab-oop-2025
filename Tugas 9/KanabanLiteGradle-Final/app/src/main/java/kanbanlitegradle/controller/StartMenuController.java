package kanbanlitegradle.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartMenuController {

    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> bukaHalaman("/Login.fxml"));
        registerButton.setOnAction(e -> bukaHalaman("/Register.fxml"));
        exitButton.setOnAction(e -> Platform.exit());

    }

    private void bukaHalaman(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}