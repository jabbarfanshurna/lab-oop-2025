package kanbanlitegradle.model;

import java.util.ArrayList;
import java.util.List;

public class AppContext {
    private static User currentUser;
    private static Project currentProject;
    private static boolean editable = false;

    private static List<User> userList = new ArrayList<>();
    private static List<Project> projectList = new ArrayList<>();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static void setUserList(List<User> users) {
        if (users == null) {
            userList = new ArrayList<>();
        } else {
            userList = users;
        }
    }

    public static void addUser(User user) {
        userList.add(user);
    }

    public static void clearUsers() {
        userList.clear();
    }

    public static Project getCurrentProject() {
        return currentProject;
    }

    public static void setCurrentProject(Project project) {
        currentProject = project;
    }

    public static List<Project> getProjectList() {
        return projectList;
    }

    public static void setProjectList(List<Project> projects) {
        projectList = projects;
    }

    public static void addProject(Project project) {
        projectList.add(project);
    }

    public static void clearProjects() {
        projectList.clear();
    }

    public static boolean isEditable() {
        return editable;
    }

    public static void setEditable(boolean isEditable) {
        editable = isEditable;
    }
}