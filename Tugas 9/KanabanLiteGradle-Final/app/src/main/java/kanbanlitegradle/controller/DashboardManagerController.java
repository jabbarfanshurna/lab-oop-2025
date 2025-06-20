package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kanbanlitegradle.model.AppContext;
import kanbanlitegradle.model.Project;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.List;

public class DashboardManagerController {

    @FXML
    private VBox projectListContainer;

    @FXML
    public void initialize() {
        tampilkanDaftarProyek();
    }

    private void tampilkanDaftarProyek() {
        List<Project> projects = AppContext.getProjectList();
        projectListContainer.getChildren().clear();

        if (projects == null || projects.isEmpty()) {
            Label kosongLabel = new Label("Belum ada proyek.");
            kosongLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 16px;");
            projectListContainer.getChildren().add(kosongLabel);
            return;
        }

        for (Project project : projects) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(15));
            card.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #dcdcdc; -fx-border-radius: 10;");

            Label nama = new Label(project.getName());
            nama.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label deadline = new Label("Deadline: " + (project.getDeadline() != null ? project.getDeadline() : "-"));
            deadline.setStyle("-fx-text-fill: #7f8c8d;");

            HBox buttonContainer = new HBox(10);

            Button detailBtn = new Button("Lihat Detail");
            detailBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 8;");
            detailBtn.setOnAction(e -> {
                AppContext.setCurrentProject(project);
                bukaHalamanDetailProyek();
            });

            Button editBtn = new Button("Edit");
            editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 8;");
            editBtn.setOnAction(e -> {
                AppContext.setCurrentProject(project);
                bukaHalamanEditProyek();
            });

            Button manageBtn = new Button("Atur Team");
            manageBtn.setStyle(
                    "-fx-background-color:rgb(224, 18, 243); -fx-text-fill: white; -fx-background-radius: 8;");
            manageBtn.setOnAction(e -> {
                AppContext.setCurrentProject(project);
                openManageTeamDialog(project);
            });

            Button hapusBtn = new Button("Hapus");
            hapusBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 8;");
            hapusBtn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Hapus");
                alert.setHeaderText("Yakin ingin menghapus proyek ini?");
                alert.setContentText(project.getName());

                if (alert.showAndWait().get() == ButtonType.OK) {
                    AppContext.getProjectList().remove(project);
                    tampilkanDaftarProyek();
                }
            });

            buttonContainer.getChildren().addAll(detailBtn, editBtn, hapusBtn, manageBtn);
            card.getChildren().addAll(nama, deadline, buttonContainer);
            projectListContainer.getChildren().add(card);
        }
    }

    private void bukaHalamanDetailProyek() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/KanbanBoard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) projectListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Detail Proyek");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal membuka detail proyek.");
        }
    }

    private void openManageTeamDialog(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManageTeamDialog.fxml"));
            Parent root = loader.load();

            ManageTeamDialogController controller = loader.getController();

            controller.setProject(project);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Kelola Tim");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bukaHalamanEditProyek() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProyek.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) projectListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Proyek");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal membuka form edit proyek.");
        }
    }

    @FXML
    private void handleTambahProyek() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TambahProyek.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) projectListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tambah Proyek");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal membuka form tambah proyek.");
        }
    }

    @FXML
    private void handleLogout() {
        AppContext.setCurrentUser(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) projectListContainer.getScene().getWindow();
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