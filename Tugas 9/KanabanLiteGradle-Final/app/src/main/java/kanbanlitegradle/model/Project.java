package kanbanlitegradle.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String id;
    private String name;
    private String deskripsi;
    private LocalDate deadline;
    private String status;

    private List<String> teamMembers;
    private List<Task> tasks;

    public Project() {
        this.teamMembers = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public Project(String id, String name, String deskripsi, LocalDate deadline, String status,
            List<String> teamMembers, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.deskripsi = deskripsi;
        this.deadline = deadline;
        this.status = status;
        this.teamMembers = teamMembers != null ? teamMembers : new ArrayList<>();
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    public Project(String id, String name) {
        this(id, name, "", null, "Belum Dimulai", new ArrayList<>(), new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addMember(String username) {
        if (username != null && !teamMembers.contains(username)) {
            teamMembers.add(username);
        }
    }

    public void removeMember(String username) {
        teamMembers.remove(username);
    }

    public void addTask(Task task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}