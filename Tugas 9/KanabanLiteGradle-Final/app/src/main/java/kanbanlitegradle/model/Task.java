package kanbanlitegradle.model;

import java.time.LocalDate;

public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private String assignee;
    private LocalDate deadline;

    public Task() {
    }

    public Task(String id, String title, String description, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = TaskStatus.TODO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}