package client;

public class User {
    String username;
    String email;
    boolean isAuthenticated = false;
    boolean isStudent = true;

    public void logout() {
        this.username = "";
        this.email = "";
        this.isAuthenticated = false;
    }

    public void login(String username, String email, boolean isStudent) {
        this.username = username;
        this.email = email;
        this.isStudent = isStudent;
        this.isAuthenticated = true;
    }
}