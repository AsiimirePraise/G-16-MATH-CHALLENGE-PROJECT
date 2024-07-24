package client;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClientController {
    User user;

    public ClientController(User user) {
        this.user = user;
    }

    private User login(JSONObject response) {
        // logic to interpret server response in attempt to login
        if (response.getBoolean("status")) {
            this.user.username = response.getString("username");
            this.user.email = response.getString("email");
            this.user.regNo = response.getString("regNo");
            this.user.schoolName = response.getString("schoolName");
            this.user.isStudent = response.getBoolean("isStudent");
            this.user.isAuthenticated = response.getBoolean("isAuthenticated");
            this.user.output = "[+] Successfully logged in as a " + this.user.username + (this.user.isStudent ? "(student)" : "(representative)");
        } else {
            this.user.output = "[-] " + response.get("reason").toString();
        }
        return this.user;
    }

    private User register(JSONObject response) {
        // logic to interpret server response in attempt to register
        if (response.getBoolean("status")) {
            this.user.output = "[+] " + response.get("reason").toString();
        } else {
            this.user.output = "[-] " + response.get("reason").toString();
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

    private User viewApplicants(JSONObject response) {
        // logic to interpret server response in attempt to view applicants
        JSONArray participants = new JSONArray(response.getString("applicants"));

        if (participants.isEmpty()) {
            this.user.output = "[-] No pending participant registration requests";
            return this.user;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.user.schoolName.strip().toUpperCase() + " (registration number: " + this.user.regNo + ")\n");
        stringBuilder.append("\n");
        stringBuilder.append("Pending applicants:\n");

        int count = 1;
        for (int i = 0; i < participants.length(); i++) {
            JSONObject participant = new JSONObject(((JSONObject) participants.get(i)).toString(4));
            stringBuilder.append(count + ". " + participant.getString("firstname") + " " + participant.getString("lastname") + " " + participant.getString("emailAddress") + "\n");
            count++;
        }

        stringBuilder.append("\n");
        stringBuilder.append("Confirm a student using the commands\n");
        stringBuilder.append(" - confirm yes <username>\n");
        stringBuilder.append(" - confirm no <username>\n");

        this.user.output = stringBuilder.toString();

        return this.user;
    }

    public User exec(String responseData) {
        JSONObject response = new JSONObject(responseData);
        switch (response.get("command").toString()) {
            case "login" -> {
                return this.login(response);
            }
            case "register" -> {
                return this.register(response);
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
                return this.viewApplicants(response);
            }
            default -> throw new IllegalStateException("Invalid response");
        }
    }
}