package server;

import org.json.*;

public class Controller {
    JSONObject obj;

    public Controller(JSONObject obj) {
        this.obj = obj;
    }

    private JSONObject login(JSONObject obj) {
        // logic to log in a user check the student db and then the representatives (!isAuthenticated)
        JSONObject output = new JSONObject();
        if (obj.getBoolean("isAuthenticated")) {
            output.put("reason", "user is already authenticated");
            output.put("status", false);
            return output;
        }

        output.put("command", "login");


        // check the given credentials
        Object arr = obj.get("tokens");
        JSONArray tokens = new JSONArray(arr.toString());
        String username = (String) tokens.get(1);
        String email = (String) tokens.get(2);
        output.put("status", true);
        output.put("userId", 1);
        output.put("isStudent", true);
        output.put("username", username);
        output.put("email", email);
        return output;
    }

    private JSONObject register(JSONObject obj) {
        // logic to register student this can work with isAuthenticated == false only (!isAuthenticated)
        return new JSONObject();
    }

    private JSONObject attemptChallenge(JSONObject obj) {
        // logic to attempt a challenge respond with the random questions if user is eligible (student, isAuthenticated)
        return new JSONObject();
    }

    private JSONObject viewChallenges(JSONObject obj) {
        // logic to view challenges available and can be attempted (student, isAuthenticated)
        return new JSONObject();
    }

    private JSONObject confirm(JSONObject obj) {
        // logic to confirm registered students (representatives, isAuthenticated)
        return new JSONObject();
    }

    private JSONObject viewApplicants(JSONObject obj) {
        // logic to confirm registered students (representatives, isAuthenticated)
        return new JSONObject();
    }

    public JSONObject run() {
        switch (this.obj.get("command").toString()) {
            case "login":
                // call login logic
                return this.login(this.obj);
            case "register":
                // call login logic
                return this.register(this.obj);
            case "viewChallenges":
                // call login logic
                return this.viewApplicants(this.obj);
            case "attemptChallenge":
                // call login logic
                return this.attemptChallenge(this.obj);
            case "confirm":
                // call login logic
                return this.confirm(this.obj);
            case "viewApplicants":
                return this.viewApplicants(this.obj);
            default:
                // command unresolved
                JSONObject outputObj = new JSONObject();
                outputObj.put("command", "exception");
                outputObj.put("reason", "Invalid command");
                return outputObj;
        }
    }
}