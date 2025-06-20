package kanbanlitegradle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kanbanlitegradle.model.*;

import java.util.List;

public class ManageTeamDialogController {

    @FXML
    private ComboBox<String> memberComboBox;
    @FXML
    private ListView<String> memberListView;

    private Project currentProject;

    public void setProject(Project project) {
        this.currentProject = project;
        loadAvailableMembers();
        refreshMemberList();
    }

    private void loadAvailableMembers() {
        List<String> available = UserDataStore.loadUsers().stream()
                .filter(u -> u instanceof Member)
                .map(User::getUsername)
                .filter(username -> !currentProject.getTeamMembers().contains(username))
                .toList();

        memberComboBox.getItems().setAll(available);
    }

    private void refreshMemberList() {
        memberListView.getItems().setAll(currentProject.getTeamMembers());
    }

    @FXML
    private void handleAddMember() {
        String selected = memberComboBox.getValue();
        if (selected != null) {
            currentProject.addMember(selected);
            refreshMemberList();
            loadAvailableMembers();
            memberComboBox.setValue(null);

            ProjectDataStore.saveProjects(AppContext.getProjectList());
        } else {
            showAlert("Pilih member terlebih dahulu.");
        }
    }

    @FXML
    private void handleClose() {
        ((Stage) memberListView.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}