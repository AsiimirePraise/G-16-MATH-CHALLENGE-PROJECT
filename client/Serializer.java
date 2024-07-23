package client;

import org.json.JSONObject;

import java.util.Scanner;

public class Serializer {
    boolean isAuthenticated;
    boolean isStudent;
    public Serializer(boolean isAuthenticated, boolean isStudent) {
        this.isAuthenticated = isAuthenticated;
        this.isStudent = isStudent;
    }

    public String login() {
        if (this.isAuthenticated) {
            return "Session already authenticated";
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter user login details (username and email)");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.println("\n");
        String[] tokens = new String[3];
        tokens[0] = "login";
        tokens[1] = username;
        tokens[2] = email;

        JSONObject obj = new JSONObject();
        obj.put("command", "login");
        obj.put("isAuthenticated", false);
        obj.put("tokens", tokens);
        obj.put("isStudent", false);

        return obj.toString(4);
    }
    public String register(String[] arr) {
        JSONObject obj = new JSONObject();
        obj.put("command", "register");
        obj.put("isAuthenticated", false);
        obj.put("tokens", arr);
        obj.put("isStudent", true);
        return obj.toString(4);
    }
    public void logout() {
        this.isAuthenticated = false;
    }
    public String serialize(String command) {
        String[] tokens = command.split("\\s+");
        if (!isAuthenticated && tokens[0].equals("register")) {
            return this.register(tokens);
        }

        switch (tokens[0]) {
            case "login":
                return this.login();

            case "logout":
                this.logout();
                break;
            default:
                return "Invalid command";
        }
        return "";
    }
    public static void main (String[] args) {
        Serializer sample = new Serializer(false, false);
        sample.serialize("login frank ogenrwothjimfrank@gmail.com");
    }
}