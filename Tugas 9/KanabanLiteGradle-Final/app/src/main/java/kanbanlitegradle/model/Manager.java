package kanbanlitegradle.model;

public class Manager extends User {

    public Manager(String username, String password, String role) {
        super(username, password, role);
    }

    @Override
    public String getRole() {
        return "manager";
    }
}