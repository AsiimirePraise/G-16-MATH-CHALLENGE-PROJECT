package client;

import org.json.JSONObject;

public class Serializer {
    boolean isAuthenticated;
    boolean isStudent;

    public Serializer(boolean isAuthenticated, boolean isStudent) {
        this.isAuthenticated = isAuthenticated;
        this.isStudent = isStudent;
    }

    public String login(String[] arr) {
        if (this.isAuthenticated) {
            return "Session already authenticated";
        }

        JSONObject obj = new JSONObject();
        obj.put("command", "login");
        obj.put("isAuthenticated", false);
        obj.put("tokens", arr);
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
                return this.login(tokens);
            case "logout":
                this.logout();
                break;
            default:
                return "Invalid command";
        }
        return "";
    }

    public static void main(String[] args) {
        Serializer sample = new Serializer(false, false);
        sample.serialize("login frank ogenrwothjimfrank@gmail.com");
    }
}