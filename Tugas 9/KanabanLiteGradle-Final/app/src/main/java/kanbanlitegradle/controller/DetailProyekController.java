package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kanbanlitegradle.model.Project;
import kanbanlitegradle.model.Task;

public class DetailProyekController {

    @FXML
    private VBox belumDimulaiBox;

    @FXML
    private VBox sedangDikerjakanBox;

    @FXML
    private VBox selesaiBox;

    private Project project;

    public void setProject(Project project) {
        this.project = project;
        tampilkanTask();
    }

    private void tampilkanTask() {
        belumDimulaiBox.getChildren().clear();
        sedangDikerjakanBox.getChildren().clear();
        selesaiBox.getChildren().clear();

        for (Task task : project.getTasks()) {
            Label label = new Label(task.getTitle());
            label.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 8px; -fx-border-color: gray;");

            switch (task.getStatus()) {
                case TODO:
                    belumDimulaiBox.getChildren().add(label);
                    break;
                case IN_PROGRESS:
                    sedangDikerjakanBox.getChildren().add(label);
                    break;
                case DONE:
                    selesaiBox.getChildren().add(label);
                    break;
            }
        }
    }

    @FXML
    private void handleKembali() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/LihatSemuaProyek.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) belumDimulaiBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lihat Semua Proyek");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}