package kanbanlitegradle.controller;

import java.time.LocalDate;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kanbanlitegradle.model.AppContext;
import kanbanlitegradle.model.Project;
import kanbanlitegradle.model.ProjectDataStore;

public class TambahProyekController {

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private TextField namaField;

    @FXML
    private TextArea deskripsiArea;

    @FXML
    private void handleBatal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardManager.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Manager");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal kembali ke dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        String nama = namaField.getText();
        String deskripsi = deskripsiArea.getText();
        LocalDate deadline = deadlinePicker.getValue();

        if (nama.isEmpty() || deskripsi.isEmpty() || deadline == null) {
            showAlert("Semua field harus diisi termasuk deadline!");
            return;
        }

        Project project = new Project(UUID.randomUUID().toString(), nama);
        project.setDeskripsi(deskripsi);
        project.setDeadline(deadline);
        project.setStatus("Belum Dimulai");

        AppContext.addProject(project);
        ProjectDataStore.saveProjects(AppContext.getProjectList());

        showAlert("Proyek berhasil disimpan!");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardManager.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Manager");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal kembali ke dashboard: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}