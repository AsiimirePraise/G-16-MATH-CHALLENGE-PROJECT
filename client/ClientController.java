package client;

import org.json.JSONObject;

public class ClientController {
    User user;
    JSONObject response;

    public ClientController(User user, String response) {
        this.user = user;
        this.response = new JSONObject(response);
    }

    private User login() {
        // logic to interpret server response in attempt to login
        return new User();
    }

    private User register(JSONObject response) {
        // logic to interpret server response in attempt to register
        if (response.getBoolean("status")) {
            this.user.output = "success:: " + response.get("reason").toString();
        } else {
            this.user.output = "fail:: " + response.get("reason").toString();
        }
        return this.user;
    }

    private User attemptChallenge() {
        // logic to interpret server response in attempt to attempt challenge
        return new User();
    }

    private User viewChallenges() {
        // logic to interpret server response in attempt to view challenges
        return new User();
    }

    private User confirm() {
        // logic to interpret server response in attempt to confirm
        return new User();
    }

    private User viewApplicants() {
        // logic to interpret server response in attempt to view applicants
        return new User();
    }

    public User exec() {
        switch (this.response.get("command").toString()) {
            case "login" -> {
                return this.login();
            }
            case "register" -> {
                return this.register(this.response);
            }
            case "attemptChallenge" -> {
                return this.attemptChallenge();
            }
            case "viewChallenges" -> {
                return this.viewChallenges();
            }
            case "confirm" -> {
                return this.confirm();
            }
            case "viewApplicants" -> {
                return this.viewApplicants();
            }
            default -> throw new IllegalStateException("Invalid response");
        }
    }
}