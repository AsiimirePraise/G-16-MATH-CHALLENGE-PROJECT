package server;

import org.json.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    JSONObject obj;

    public Controller(JSONObject obj) {
        this.obj = obj;
    }

    private JSONObject login(JSONObject obj) throws SQLException, ClassNotFoundException {
        // logic to log in a student this can work with isAuthenticated == false only (!isAuthenticated)
        DbConnection dbConnection = new DbConnection();
        JSONArray tokens = obj.getJSONArray("tokens");
        String username = tokens.get(1).toString();
        String email = tokens.get(2).toString();
        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "login");
        clientResponse.put("username", username);
        clientResponse.put("email", email);
        String readParticipantQuery = "SELECT * FROM participant";
        ResultSet participantResultSet = dbConnection.read(readParticipantQuery);
        while (participantResultSet.next()) {
            if (username.equals(participantResultSet.getString("username")) && email.equals(participantResultSet.getString("emailAddress"))) {
                // there is a match here

                String regNo = participantResultSet.getString("regNo");

                clientResponse.put("regNo", regNo);
                clientResponse.put("isStudent", true);
                clientResponse.put("isAuthenticated", true);
                clientResponse.put("status", true);
                return clientResponse;
            }
        }
        String readRepresentativeQuery = "SELECT * FROM school";
        ResultSet representativeResultSet = dbConnection.read(readRepresentativeQuery);
        while (representativeResultSet.next()) {
            if (username.equals(representativeResultSet.getString("representativeName")) && email.equals(representativeResultSet.getString("representativeEmail"))) {
                // there is a match

                String regNo = representativeResultSet.getString("regNo");

                clientResponse.put("regNo", regNo);
                clientResponse.put("isStudent", false);
                clientResponse.put("isAuthenticated", true);
                clientResponse.put("status", true);
                return clientResponse;
            }
        }
        clientResponse.put("isStudent", false);
        clientResponse.put("isAuthenticated", false);
        clientResponse.put("status", false);
        clientResponse.put("reason", "Invalid credentials. check the details provided");
        return clientResponse;
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

    public JSONObject run() throws IOException, SQLException, ClassNotFoundException {
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