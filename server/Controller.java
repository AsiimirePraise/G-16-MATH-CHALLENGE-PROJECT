package server;

import org.json.JSONObject;

public class Controller {
    JSONObject obj;

    public Controller(JSONObject obj) {
        this.obj = obj;
    }
    private JSONObject login(JSONObject obj) {
        // logic to log in a user check the student db and then the representatives (!isAuthenticated)
        return new JSONObject();
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