package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kanbanlitegradle.model.AppContext;
import kanbanlitegradle.model.Project;

import java.time.LocalDate;

public class EditProyekController {

    @FXML
    private TextField namaField;

    @FXML
    private DatePicker deadlinePicker;

    private Project currentProject;

    @FXML
    public void initialize() {
        currentProject = AppContext.getCurrentProject();
        if (currentProject != null) {
            namaField.setText(currentProject.getName());
            deadlinePicker.setValue(currentProject.getDeadline());
        }
    }

    @FXML
    private void handleSimpan() {
        String nama = namaField.getText().trim();
        LocalDate deadline = deadlinePicker.getValue();

        if (nama.isEmpty()) {
            showAlert("Nama proyek tidak boleh kosong.");
            return;
        }

        currentProject.setName(nama);
        currentProject.setDeadline(deadline);

        showAlert("Proyek berhasil diperbarui.");
        kembaliKeDashboard();
    }

    @FXML
    private void handleKembali() {
        kembaliKeDashboard();
    }

    private void kembaliKeDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardManager.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Manager");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal kembali ke dashboard.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}