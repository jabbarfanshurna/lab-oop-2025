package kanbanlitegradle;

import javafx.application.Application;
import javafx.stage.Stage;
import kanbanlitegradle.model.AppContext;
import kanbanlitegradle.model.ProjectDataStore;
import kanbanlitegradle.model.UserDataStore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        UserDataStore.initializeUsers();
        AppContext.setUserList(UserDataStore.loadUsers());

        AppContext.setProjectList(ProjectDataStore.loadProjects());

        Parent root = FXMLLoader.load(getClass().getResource("/StartMenu.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("KanbanLite");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}