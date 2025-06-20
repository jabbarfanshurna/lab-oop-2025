package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kanbanlitegradle.model.*;

import java.util.UUID;

public class KanbanBoardController {

    @FXML
    private Label judulProyekLabel;
    @FXML
    private Label deadlineProyekLabel;
    @FXML
    private VBox belumDimulaiBox, sedangBerjalanBox, selesaiBox;
    @FXML
    private TextField taskNameField;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private ComboBox<String> memberChoiceBox;
    @FXML
    private Region spacer;
    @FXML
    private Button tambahTaskButton;

    private Project project;
    private User currentUser;

    @FXML
    public void initialize() {
        project = AppContext.getCurrentProject();
        currentUser = AppContext.getCurrentUser();

        if (project == null || currentUser == null) {
            showAlert("Proyek atau pengguna tidak tersedia.");
            return;
        }

        judulProyekLabel.setText(project.getName());
        deadlineProyekLabel.setText(
                project.getDeadline() != null ? "Deadline: " + project.getDeadline().toString() : "Deadline: -");

        statusChoiceBox.getItems().addAll("Belum Dimulai", "Sedang Berjalan", "Selesai");
        statusChoiceBox.setValue("Belum Dimulai");

        memberChoiceBox.getItems().clear();
        for (String username : project.getTeamMembers()) {
            memberChoiceBox.getItems().add(username);
        }

        if (!(currentUser instanceof Manager)) {
            taskNameField.setVisible(false);
            statusChoiceBox.setVisible(false);
            memberChoiceBox.setVisible(false);
            tambahTaskButton.setVisible(false);
            spacer.setVisible(false);
        }

        tampilkanSemuaTask();
    }

    private void tampilkanSemuaTask() {
        belumDimulaiBox.getChildren().clear();
        sedangBerjalanBox.getChildren().clear();
        selesaiBox.getChildren().clear();

        for (Task task : project.getTasks()) {
            Label titleLabel = new Label(task.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label memberLabel = new Label();
            if (task.getAssignee() != null) {
                memberLabel.setText("Ditugaskan ke: " + task.getAssignee());
                memberLabel.setStyle("-fx-text-fill: #7f8c8d;");
            }

            VBox taskCard = new VBox(5, titleLabel, memberLabel);
            taskCard.setPadding(new Insets(10));
            taskCard.setStyle(
                    "-fx-background-color: #ffffff;" +
                            "-fx-border-color: #dcdcdc;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 2);");

            HBox actionButtons = new HBox(10);
            actionButtons.setAlignment(Pos.CENTER_RIGHT);

            if (currentUser instanceof Manager) {
                Button hapusBtn = new Button("Hapus");
                Button ubahStatusBtn = new Button("Ubah Status");

                hapusBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                ubahStatusBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

                hapusBtn.setOnAction(e -> {
                    project.getTasks().remove(task);
                    ProjectDataStore.saveProjects(AppContext.getProjectList());
                    tampilkanSemuaTask();
                });

                ubahStatusBtn.setOnAction(e -> {
                    ubahStatusTask(task);
                    ProjectDataStore.saveProjects(AppContext.getProjectList());
                });

                actionButtons.getChildren().addAll(ubahStatusBtn, hapusBtn);
            }

            if (currentUser instanceof Member) {
                if (task.getAssignee() != null && task.getAssignee().equals(currentUser.getUsername())) {
                    Button ubahStatusBtn = new Button("Ubah Status");
                    ubahStatusBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                    ubahStatusBtn.setOnAction(e -> {
                        ubahStatusTask(task);
                        ProjectDataStore.saveProjects(AppContext.getProjectList());
                    });
                    actionButtons.getChildren().add(ubahStatusBtn);
                } else {
                    Label notMyTaskLabel = new Label("Bukan tugas Anda");
                    notMyTaskLabel.setStyle("-fx-text-fill: #bdc3c7; -fx-font-style: italic;");
                    taskCard.getChildren().add(notMyTaskLabel);
                }
            }

            if (!actionButtons.getChildren().isEmpty()) {
                taskCard.getChildren().add(actionButtons);
            }

            switch (task.getStatus()) {
                case TODO -> belumDimulaiBox.getChildren().add(taskCard);
                case IN_PROGRESS -> sedangBerjalanBox.getChildren().add(taskCard);
                case DONE -> selesaiBox.getChildren().add(taskCard);
            }
        }
    }

    @FXML
    private void handleTambahTask() {
        String nama = taskNameField.getText();
        String statusText = statusChoiceBox.getValue();
        String selectedMember = memberChoiceBox.getValue();

        if (nama == null || nama.trim().isEmpty()) {
            showAlert("Nama task tidak boleh kosong.");
            return;
        }

        TaskStatus status = switch (statusText) {
            case "Sedang Berjalan" -> TaskStatus.IN_PROGRESS;
            case "Selesai" -> TaskStatus.DONE;
            default -> TaskStatus.TODO;
        };

        Task taskBaru = new Task(UUID.randomUUID().toString(), nama, "", selectedMember);
        taskBaru.setStatus(status);

        project.addTask(taskBaru);
        ProjectDataStore.saveProjects(AppContext.getProjectList());

        tampilkanSemuaTask();

        taskNameField.clear();
        statusChoiceBox.setValue("Belum Dimulai");
        memberChoiceBox.setValue(null);
    }

    private void ubahStatusTask(Task task) {
        TaskStatus nextStatus = switch (task.getStatus()) {
            case TODO -> TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> TaskStatus.DONE;
            case DONE -> TaskStatus.TODO;
        };
        task.setStatus(nextStatus);
        tampilkanSemuaTask();
    }

    @FXML
    private void handleKembali() {
        try {
            FXMLLoader loader = new FXMLLoader();
            if (currentUser instanceof Manager) {
                loader.setLocation(getClass().getResource("/DashboardManager.fxml"));
            } else {
                loader.setLocation(getClass().getResource("/DashboardMember.fxml"));
            }
            Parent root = loader.load();
            Stage stage = (Stage) taskNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal kembali ke dashboard.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}