package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import kanbanlitegradle.model.AppContext;
import kanbanlitegradle.model.Project;
import kanbanlitegradle.model.User;
import javafx.geometry.Insets;

import java.util.List;

public class DashboardMemberController {

    @FXML
    private VBox memberProjectListContainer;

    @FXML
    public void initialize() {
        tampilkanProyekMember();
    }

    private void tampilkanProyekMember() {
        User currentUser = AppContext.getCurrentUser();
        String currentUsername = currentUser.getUsername();
        List<Project> allProjects = AppContext.getProjectList();
        memberProjectListContainer.getChildren().clear();

        for (Project project : allProjects) {
            boolean isMember = project.getTeamMembers().contains(currentUsername);

            VBox card = new VBox(8);
            card.setPadding(new Insets(15));
            card.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #dcdcdc; -fx-border-radius: 10;");

            Label nama = new Label(project.getName());
            nama.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label deadline = new Label("Deadline: " + (project.getDeadline() != null ? project.getDeadline() : "-"));
            deadline.setStyle("-fx-text-fill: #7f8c8d;");

            Label statusLabel = new Label();
            if (isMember) {
                statusLabel.setText("✅ Anda tergabung dalam proyek ini.");
                statusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");
            } else {
                statusLabel.setText("⚠ Anda tidak tergabung dalam proyek ini.");
                statusLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 12px;");
            }

            Button lihatBtn = new Button(isMember ? "Lihat Proyek" : "Lihat Saja");
            lihatBtn.setStyle(isMember
                    ? "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 8;"
                    : "-fx-background-color: #bdc3c7; -fx-text-fill: #2c3e50; -fx-background-radius: 8;");
            lihatBtn.setOnAction(e -> {
                AppContext.setCurrentProject(project);
                AppContext.setEditable(isMember);
                bukaHalamanKanban();
            });

            card.getChildren().addAll(nama, deadline, statusLabel, lihatBtn);
            memberProjectListContainer.getChildren().add(card);
        }

        if (allProjects.isEmpty()) {
            Label kosong = new Label("Belum ada proyek tersedia.");
            kosong.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 16px;");
            memberProjectListContainer.getChildren().add(kosong);
        }
    }

    private void bukaHalamanKanban() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/KanbanBoard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) memberProjectListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Kanban Board");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal membuka Kanban Board.");
        }
    }

    @FXML
    private void handleLogout() {
        AppContext.setCurrentUser(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) memberProjectListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal logout.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}