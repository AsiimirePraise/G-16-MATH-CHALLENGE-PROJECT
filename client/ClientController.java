package client;

import org.json.JSONArray;
import org.json.JSONObject;
import server.Randomizer;

public class ClientController {
    User user;

    public ClientController(User user) {
        this.user = user;
    }

    private User login(JSONObject response) {
        // logic to interpret server response in attempt to login
        if (response.getBoolean("status")) {
            this.user.id = response.getInt("participant_id");
            this.user.username = response.getString("username");
            this.user.email = response.getString("email");
            this.user.regNo = response.getString("registration_number");
            this.user.schoolName = response.getString("schoolName");
            this.user.isStudent = response.getBoolean("isStudent");

            System.out.println("=====================================================");
            System.out.println("       WELCOME TO THE PORTAL " + this.user.username.toUpperCase() + "      ");
            System.out.println("=====================================================");
            System.out.println("\nThank you for logging in. You are logged in as a " + (this.user.isStudent ? "student" : "representative") + "\n");
            System.out.println("As a " + (this.user.isStudent ? "student" : "representative") + " you can follow the commands below to navigate the portal:\n");

            if (response.getBoolean("isStudent")) {
                System.out.println("\n1. View Challenges [to view the different open challenges in the system]:");
                System.out.println("   viewChallenges");
                System.out.println("\n2. Attempt Challenge [to attempt a particular open challenge]:");
                System.out.println("   attemptChallenge <challenge_id>");
                System.out.println("\n=====================================================\n");
            } else {
                System.out.println("\n1. View Applicants [to view registering participants]:");
                System.out.println("   viewApplicants");
                System.out.println("\n2. Confirm Applicant [to confirm participant registration]:");
                System.out.println("   confirm <yes/no> <applicant_username>");
                System.out.println("\n=====================================================\n");
            }
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

    private User attemptChallenge(JSONObject response) {
        // logic to interpret server response in attempt to attempt challenge
        if (!response.getBoolean("status")) {
            this.user.output = response.getString("reason");
            return this.user;
        }
        JSONArray allQuestions = response.getJSONArray("questions");
        if (allQuestions.isEmpty()) {
            this.user.output = "[-] No available questions in this challenge right now";
            return this.user;
        }
        this.user.output = response.toString();
        return this.user;
    }

    private User viewChallenges(JSONObject response) {
        // logic to interpret server response in attempt to view challenges
        JSONArray challenges = new JSONArray(response.getString("challenges"));
        if (challenges.isEmpty()) {
            this.user.output = "[-] No open challenges are available right now";
            return this.user;
        }
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("===================================\n");
        stringBuilder.append("             CHALLENGES            \n");
        stringBuilder.append("===================================\n\n");

        for (int i = 0; i < challenges.length(); i++) {
            JSONObject challenge = new JSONObject(((JSONObject) challenges.get(i)).toString(4));
            stringBuilder.append("Challenge ID     : " + challenge.get("id") + "\n");
            stringBuilder.append("Challenge Name   : " + challenge.getString("name") + "\n");
            stringBuilder.append("Difficulty       : " + challenge.getString("difficulty") + "\n");
            stringBuilder.append("Closing Date     : " + challenge.getString("closing_date") + "\n");
            stringBuilder.append("Duration (mins)  : " + challenge.getInt("time_allocation") + "\n");
            stringBuilder.append("-----------------------------------\n\n");
        }

        stringBuilder.append("To attempt a particular challenge, use the command:\n");
        stringBuilder.append("-> attemptChallenge <challenge_id>\n\n");

        System.out.println(stringBuilder.toString());

        this.user.output = stringBuilder.toString();

        return this.user;
    }

    private User confirm(JSONObject response) {
        // logic to interpret server response in attempt to confirm
        if (response.getBoolean("status")) {
            this.user.output = response.getString("reason");
        } else {
            this.user.output = response.getString("reason");
        }
        return this.user;
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
            stringBuilder.append(count + ". " + participant.getString("username") + " " + participant.getString("email_address") + "\n");
            count++;
        }
        stringBuilder.append("\n");
        stringBuilder.append("Confirm a student using the commands\n");
        stringBuilder.append(" - confirm yes <username>\n");
        stringBuilder.append(" - confirm no <username>\n");
        this.user.output = stringBuilder.toString();
        return this.user;
    }

    private User attempt(JSONObject response) {
        this.user.output = response.getString("reason");
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
                return this.attemptChallenge(response);
            }
            case "viewChallenges" -> {
                return this.viewChallenges(response);
            }
            case "confirm" -> {
                return this.confirm(response);
            }
            case "attempt" -> {
                return this.attempt(response);
            }
            case "viewApplicants" -> {
                return this.viewApplicants(response);
            }
            default -> throw new IllegalStateException("Invalid response");
        }
    }
}