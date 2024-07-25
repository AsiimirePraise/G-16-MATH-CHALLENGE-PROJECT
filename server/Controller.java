package server;

import org.json.*;

import javax.mail.MessagingException;
import java.io.FileOutputStream;
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
        String readParticipantQuery = "SELECT * FROM participants";
        ResultSet participantResultSet = dbConnection.read(readParticipantQuery);
        while (participantResultSet.next()) {
            if (username.equals(participantResultSet.getString("username")) && email.equals(participantResultSet.getString("email_address"))) {
                // there is a match here
                String regNo = participantResultSet.getString("registration_number");
                clientResponse.put("participant_id", participantResultSet.getInt("id"));
                clientResponse.put("registration_number", regNo);
                clientResponse.put("schoolName", "undefined");
                clientResponse.put("isStudent", true);
                clientResponse.put("isAuthenticated", true);
                clientResponse.put("status", true);
                return clientResponse;
            }
        }
        String readRepresentativeQuery = "SELECT * FROM schools";
        ResultSet representativeResultSet = dbConnection.read(readRepresentativeQuery);
        while (representativeResultSet.next()) {
            if (username.equals(representativeResultSet.getString("representative_name")) && email.equals(representativeResultSet.getString("representative_email"))) {
                // there is a match
                String schoolName = representativeResultSet.getString("name");
                String regNo = representativeResultSet.getString("registration_number");
                clientResponse.put("participant_id", 0);
                clientResponse.put("schoolName", schoolName);
                clientResponse.put("registration_number", regNo);
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

    private JSONObject register(JSONObject obj) throws IOException, MessagingException, SQLException, ClassNotFoundException {
        // logic to register student this can work with isAuthenticated == false only (!isAuthenticated)
        Email emailAgent = new Email();
        DbConnection dbConnection = new DbConnection();
        JSONArray tokens = obj.getJSONArray("tokens");
        JSONObject participantObj = new JSONObject();
        participantObj.put("username", tokens.get(1));
        participantObj.put("firstname", tokens.get(2));
        participantObj.put("lastname", tokens.get(3));
        participantObj.put("email_address", tokens.get(4));
        participantObj.put("dob", tokens.get(5));
        participantObj.put("registration_number", tokens.get(6));
        participantObj.put("imagePath", tokens.get(7));
        participantObj.put("tokenized_image", obj.getJSONObject("tokenized_image"));
        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "register");
        ResultSet schoolAcceptance = dbConnection.read("SELECT * FROM `rejected_participants` WHERE email_address = \"" + participantObj.getString("email_address") + "\" AND registration_number = \"" + participantObj.getString("registration_number") + "\";");
        if (schoolAcceptance.next()) {
            clientResponse.put("status", false);
            clientResponse.put("reason", "You can not register under this school again as you have already been rejected before");
            return clientResponse;
        }
        ResultSet rs = dbConnection.getRepresentative(participantObj.getString("registration_number"));
        String representativeEmail;
        if (rs.next()) {
            representativeEmail = rs.getString("representative_email");
        } else {
            clientResponse.put("status", false);
            clientResponse.put("reason", "school does not exist in our database");
            return clientResponse;
        }
        LocalStorage localStorage = new LocalStorage("participants.json");
        if (!localStorage.read().toString().contains(participantObj.toString())) {
            localStorage.add(participantObj);
            clientResponse.put("status", true);
            clientResponse.put("reason", "Participant created successfully awaiting representative approval");
            emailAgent.sendParticipantRegistrationRequestEmail(representativeEmail, participantObj.getString("email_address"), participantObj.getString("username"));
            return clientResponse;
        }
        clientResponse.put("status", false);
        clientResponse.put("reason", "Participant creation failed found an existing participant object");
        return clientResponse;
    }

    private JSONObject attemptChallenge(JSONObject obj) throws SQLException, ClassNotFoundException {
        // logic to attempt a challenge respond with the random questions if user is eligible (student, isAuthenticated)
        JSONObject clientResponse = new JSONObject();
        JSONArray questions = new JSONArray();
        DbConnection dbConnection = new DbConnection();
        int challengeId = Integer.parseInt((String) new JSONArray(obj.get("tokens").toString()).get(1));
        ResultSet challengeQuestions;
        ResultSet challengeMaxAttemptQuery = dbConnection.read("SELECT new.* FROM (SELECT id, participant, challenge, COUNT(*) AS counts\n" +
                "FROM participant_challenges\n" +
                "GROUP BY participant, challenge) AS new WHERE participant=" + obj.getInt("participant") + " AND challenge = " + challengeId + ";");
        if (challengeMaxAttemptQuery.next()) {
            if (challengeMaxAttemptQuery.getInt("counts") > 2) {
                JSONObject altResponse = new JSONObject();
                altResponse.put("command", "attemptChallenge");
                altResponse.put("status", false);
                altResponse.put("reason", "[-] You have already reached the limit for attempting this challenge");
                return altResponse;
            }
        }
        ResultSet rs = dbConnection.read("SELECT challenge_name, time_allocation, start_date FROM `challenges` WHERE id = " + challengeId + " AND `start_date` <= CURRENT_DATE AND `closing_date` >= CURRENT_DATE;");
        String challengeName;
        int challengeDuration;
        if (rs.next()) {
            challengeName = rs.getString("challenge_name");
            challengeDuration = rs.getInt("time_allocation");
        } else {
            JSONObject altResponse = new JSONObject();
            altResponse.put("command", "attemptChallenge");
            altResponse.put("status", false);
            altResponse.put("reason", "[-] The requested challenge is currently not open or has expired");
            return altResponse;
        }
        challengeQuestions = dbConnection.getChallengeQuestions(challengeId);
        while (challengeQuestions.next()) {
            JSONObject question = new JSONObject();
            question.put("id", challengeQuestions.getString("id"));
            question.put("question", challengeQuestions.getString("question"));
            question.put("score", challengeQuestions.getString("score"));
            questions.put(question);
        }
        // randomize question selection and provide 1 tenth of the questions us math ceil to avoid 0 due to integer divisions
        JSONArray randomlySelectedQuestions = Randomizer.randomize(questions);
        clientResponse.put("command", "attemptChallenge");
        clientResponse.put("questions", randomlySelectedQuestions);
        clientResponse.put("challenge_id", challengeId);
        clientResponse.put("status", true);
        clientResponse.put("challenge_name", challengeName);
        clientResponse.put("time_allocation", challengeDuration);
        return clientResponse;
    }

    private JSONObject viewChallenges(JSONObject obj) throws SQLException, ClassNotFoundException {
        JSONObject clientResponse = new JSONObject();
        DbConnection dbConnection = new DbConnection();
        ResultSet availableChallenges = dbConnection.getChallenges();
        JSONArray challenges = new JSONArray();
        while (availableChallenges.next()) {
            JSONObject challenge = new JSONObject();
            challenge.put("id", availableChallenges.getInt("id"));
            challenge.put("name", availableChallenges.getString("challenge_name"));
            challenge.put("difficulty", availableChallenges.getString("difficulty"));
            challenge.put("time_allocation", availableChallenges.getInt("time_allocation"));
            challenge.put("starting_date", availableChallenges.getDate("start_date"));
            challenge.put("closing_date", availableChallenges.getDate("closing_date"));
            challenges.put(challenge);
        }
        clientResponse.put("command", "viewChallenges");
        clientResponse.put("challenges", challenges.toString());
        return clientResponse;
    }

    private JSONObject confirm(JSONObject obj) throws IOException, SQLException, ClassNotFoundException, MessagingException {
        // logic to confirm registered students (representatives, isAuthenticated)
        LocalStorage localStorage = new LocalStorage("participants.json");
        String username = obj.getString("username");
        JSONObject participant = localStorage.readEntryByUserName(username);
        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "confirm");
        if (participant.isEmpty()) {
            clientResponse.put("status", false);
            clientResponse.put("reason", "Invalid command check the username provided");
            return clientResponse;
        }
        DbConnection dbConnection = new DbConnection();
        if (obj.getBoolean("confirm")) {
            String pic_path = participant.getString("username") + "_" + participant.getString("registration_number") + ".jpg";
            JSONObject tokenObj = participant.getJSONObject("tokenized_image");
            saveProfileImage(tokenObj, pic_path);
            dbConnection.createParticipant(participant.getString("username"), participant.getString("firstname"), participant.getString("lastname"), participant.getString("email_address"), participant.getString("dob"), participant.getString("registration_number"), "participants/" + pic_path);
            localStorage.deleteEntryByUserName(username);
            clientResponse.put("reason", username + " - " + participant.getString("email_address") + " confirmed successfully");
            ResultSet rs = dbConnection.getSchool(participant.getString("registration_number"));
            if (rs.next()) {
            }
            String schoolName = rs.getString("name");
            Email email = new Email();
            email.sendParticipantConfirmedEmail(participant.getString("email_address"), participant.getString("username"), schoolName);
        } else {
            dbConnection.createParticipantRejected(participant.getString("username"), participant.getString("firstname"), participant.getString("lastname"), participant.getString("email_address"), participant.getString("dob"), participant.getString("registration_number"));
            localStorage.deleteEntryByUserName(username);
            clientResponse.put("reason", username + " - " + participant.getString("email_address") + " rejected successfully");
            ResultSet rs = dbConnection.getSchool(participant.getString("registration_number"));
            if (rs.next()) {
            }
            String schoolName = rs.getString("name");
            Email email = new Email();
            email.sendParticipantRejectedEmail(participant.getString("email_address"), participant.getString("username"), schoolName);
        }
        clientResponse.put("status", true);
        return clientResponse;
    }

    private static void saveProfileImage(JSONObject s, String pic_path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ogenr\\Documents\\lar\\recess-project\\public\\assets\\participants\\" + pic_path)) {
            JSONArray arr = s.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                byte[] buffer = jsonArrayToBytes(o.getJSONArray("buffer"));
                fileOutputStream.write(buffer, 0, o.getInt("size"));
            }
            System.out.println("file saved as " + pic_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] jsonArrayToBytes(JSONArray array) {
        byte[] bytes = new byte[array.length()];
        for (int i = 0; i < array.length(); i++) {
            bytes[i] = (byte) array.getInt(i);
        }
        return bytes;
    }

    private JSONObject viewApplicants(JSONObject obj) throws IOException {
        // logic to confirm registered students (representatives, isAuthenticated)
        String regNo = obj.getString("registration_number");
        LocalStorage localStorage = new LocalStorage("participants.json");
        String participants = localStorage.filterParticipantsByRegNo(regNo);
        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "viewApplicants");
        clientResponse.put("applicants", participants);
        return clientResponse;
    }

    public JSONObject attempt(JSONObject obj) throws SQLException, ClassNotFoundException {
        JSONArray attempt = obj.getJSONArray("attempt");
        DbConnection dbConnection = new DbConnection();

        JSONObject attemptEvaluation = new JSONObject();

        JSONObject resultObj = dbConnection.getAttemptScore(obj.getInt("challenge_id"), attempt, obj.getInt("participant_id"), obj.getInt("total_score"));

        attemptEvaluation.put("score", resultObj.getInt("score"));
        attemptEvaluation.put("participant_id", obj.getInt("participant_id"));
        attemptEvaluation.put("challenge_id", obj.getInt("challenge_id"));
        attemptEvaluation.put("total_score", obj.getInt("total_score"));
        dbConnection.createChallengeAttempt(attemptEvaluation);

        JSONObject response = new JSONObject();
        response.put("command", "attempt");
        response.put("reason", resultObj.getString("results"));

        return response;
    }

    public JSONObject run() throws IOException, SQLException, ClassNotFoundException, MessagingException {
        switch (this.obj.get("command").toString()) {
            case "login":
                // call login logic
                return this.login(this.obj);
            case "register":
                // call login logic
                return this.register(this.obj);
            case "viewChallenges":
                // call login logic
                return this.viewChallenges(this.obj);
            case "attemptChallenge":
                // call login logic
                return this.attemptChallenge(this.obj);
            case "confirm":
                // call login logic
                return this.confirm(this.obj);
            case "viewApplicants":
                return this.viewApplicants(this.obj);
            case "attempt":
                // handle attempts here
                return this.attempt(this.obj);
            default:
                // command unresolved
                JSONObject outputObj = new JSONObject();
                outputObj.put("command", "exception");
                outputObj.put("reason", "Invalid command");
                return outputObj;
        }
    }
}