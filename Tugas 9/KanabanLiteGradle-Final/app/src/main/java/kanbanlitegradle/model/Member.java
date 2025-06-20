package kanbanlitegradle.model;

public class Member extends User {

    public Member(String username, String password, String role) {
        super(username, password, role);
    }

    @Override
    public String getRole() {
        return "member";
    }

    @Override
    public String toString() {
        return getUsername();
    }
}