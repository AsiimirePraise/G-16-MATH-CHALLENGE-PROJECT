package server;

import org.json.*;

import java.io.IOException;

public class Controller {
    JSONObject obj;

    public Controller(JSONObject obj) {
        this.obj = obj;
    }

    private JSONObject login(JSONObject obj) {
        // logic to login a student this can work with isAuthenticated == false only (!isAuthenticated)
        return new JSONObject();
    }

    private JSONObject register(JSONObject obj) throws IOException {
        // logic to register student this can work with isAuthenticated == false only (!isAuthenticated)
        JSONArray tokens = obj.getJSONArray("tokens");
        JSONObject participantObj = new JSONObject();
        participantObj.put("username", tokens.get(1));
        participantObj.put("firstname", tokens.get(2));
        participantObj.put("lastname", tokens.get(3));
        participantObj.put("emailAddress", tokens.get(4));
        participantObj.put("dob", tokens.get(5));
        participantObj.put("regNo", tokens.get(6));
        participantObj.put("imagePath", tokens.get(7));

        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "register");

        LocalStorage localStorage = new LocalStorage("participants.json");
        if (!localStorage.read().toString().contains(participantObj.toString())) {
            localStorage.add(participantObj);
            clientResponse.put("status", true);
            clientResponse.put("reason", "Participant created successfully awaiting representative approval");

            return clientResponse;
        }

        clientResponse.put("status", false);
        clientResponse.put("reason", "Participant creation failed found an existing participant object");

        return clientResponse;
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

    public JSONObject run() throws IOException {
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