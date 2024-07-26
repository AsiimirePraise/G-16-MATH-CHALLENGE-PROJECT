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

        // create a connection to the mysql server
        DbConnection dbConnection = new DbConnection();

        // extract the users username and email from the tokens
        JSONArray tokens = obj.getJSONArray("tokens");
        String username = tokens.get(1).toString();
        String email = tokens.get(2).toString();

        // start constructing the response to the client query
        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "login");
        clientResponse.put("username", username);
        clientResponse.put("email", email);

        // sql to read if there is a participant that match the provided username and email
        String readParticipantQuery = "SELECT * FROM participants";
        ResultSet participantResultSet = dbConnection.read(readParticipantQuery);
        while (participantResultSet.next()) {
            if (username.equals(participantResultSet.getString("username")) && email.equals(participantResultSet.getString("email_address"))) {
                // if there is a match for the email and password .i.e the participant exists
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


        // sql to read all the schools to access the representatives
        String readRepresentativeQuery = "SELECT * FROM schools";
        ResultSet representativeResultSet = dbConnection.read(readRepresentativeQuery);
        while (representativeResultSet.next()) {
            if (username.equals(representativeResultSet.getString("representative_name")) && email.equals(representativeResultSet.getString("representative_email"))) {
                // there is a representative of the school having that email

                // get school name and registration number
                String schoolName = representativeResultSet.getString("name");
                String regNo = representativeResultSet.getString("registration_number");

                // add the other output to the client response json
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
        DbConnection dbConnection = new DbConnection();

        // extract the details provided by the user from the tokens i.e. the username, email etc
        JSONArray tokens = obj.getJSONArray("tokens");
        JSONObject participantObj = new JSONObject();
        participantObj.put("username", tokens.get(1));
        participantObj.put("firstname", tokens.get(2));
        participantObj.put("lastname", tokens.get(3));
        participantObj.put("email_address", tokens.get(4));
        participantObj.put("dob", tokens.get(5));
        participantObj.put("registration_number", tokens.get(6));
        participantObj.put("imagePath", tokens.get(7));

        // read the image bytes data
        participantObj.put("tokenized_image", obj.getJSONObject("tokenized_image"));

        // start constructing the client response
        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "register");

        // check whether this user had been rejected before proceeding to add them
        ResultSet schoolAcceptance = dbConnection.read("SELECT * FROM `rejected_participants` WHERE email_address = \"" + participantObj.getString("email_address") + "\" AND registration_number = \"" + participantObj.getString("registration_number") + "\";");
        if (schoolAcceptance.next()) {
            // return status false for a participant having record match in the rejected_participants data
            clientResponse.put("status", false);
            clientResponse.put("reason", "You can not register under this school again as you have already been rejected before");
            return clientResponse;
        }

        // read the school data via registration number to check if a school exists
        ResultSet rs = dbConnection.getRepresentative(participantObj.getString("registration_number"));
        String representativeEmail;
        if (rs.next()) {
            representativeEmail = rs.getString("representative_email");
        } else {
            // return client response in case school does not exist
            clientResponse.put("status", false);
            clientResponse.put("reason", "school does not exist in our database");
            return clientResponse;
        }

        // school exists and the user registering can go on and register

        // check if participant does not already exist
        LocalStorage localStorage = new LocalStorage("participants.json");
        if (!localStorage.read().toString().contains(participantObj.toString())) {
            // add participant to the local storage
            localStorage.add(participantObj);

            clientResponse.put("status", true);
            clientResponse.put("reason", "Participant created successfully awaiting representative approval");

            // create an image agent with the email constructor and send the registration request email
            Email emailAgent = new Email();
            emailAgent.sendParticipantRegistrationRequestEmail(representativeEmail, participantObj.getString("email_address"), participantObj.getString("username"));
            return clientResponse;
        }
        clientResponse.put("status", false);
        clientResponse.put("reason", "Participant creation failed found an existing participant object");

        return clientResponse;
    }

    private JSONObject attemptChallenge(JSONObject obj) throws SQLException, ClassNotFoundException {
        // logic to attempt a challenge respond with the random questions if user is eligible (student, isAuthenticated)

        // create the client response json and the array to hold the questions
        JSONObject clientResponse = new JSONObject();
        JSONArray questions = new JSONArray();

        DbConnection dbConnection = new DbConnection();

        // query challenges to see if someone has already reached maximum attempts
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

        // select the specified challenge to check eligibility
        ResultSet rs = dbConnection.read("SELECT challenge_name, time_allocation, start_date FROM `challenges` WHERE id = " + challengeId + " AND `start_date` <= CURRENT_DATE AND `closing_date` >= CURRENT_DATE;");
        String challengeName;
        int challengeDuration;
        if (rs.next()) {
            challengeName = rs.getString("challenge_name");
            challengeDuration = rs.getInt("time_allocation");
        } else {
            // return challenge closed
            JSONObject altResponse = new JSONObject();
            altResponse.put("command", "attemptChallenge");
            altResponse.put("status", false);
            altResponse.put("reason", "[-] The requested challenge is currently not open or has expired");
            return altResponse;
        }


        //query questions for the specific challenge
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

        // return client response data
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

        // query available open challenges
        ResultSet availableChallenges = dbConnection.getChallenges();

        // construct an array of available challenges
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

        // retrieve user from the local storage participant.json
        String username = obj.getString("username");
        JSONObject participant = localStorage.readEntryByUserName(username);

        // construct client response
        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "confirm");

        if (participant.isEmpty()) {
            // if user object does not exist in the participants.json
            clientResponse.put("status", false);
            clientResponse.put("reason", "Invalid command check the username provided");
            return clientResponse;
        }

        // initiate a db connection with the mysql
        DbConnection dbConnection = new DbConnection();

        // logic to handle action if 'yes'
        if (obj.getBoolean("confirm")) {
            String pic_path = participant.getString("username") + "_" + participant.getString("registration_number") + ".jpg";
            JSONObject tokenObj = participant.getJSONObject("tokenized_image");
            saveProfileImage(tokenObj, pic_path);

            dbConnection.createParticipant(participant.getString("username"), participant.getString("firstname"), participant.getString("lastname"), participant.getString("email_address"), participant.getString("dob"), participant.getString("registration_number"), "participants/" + pic_path);

            localStorage.deleteEntryByUserName(username);

            ResultSet userEmails = dbConnection.selectEmail(participant.getString("registration_number"));
            if (userEmails.next()) {}
            String emailA = userEmails.getString("email_address");

            Email em = new Email();

            em.sendHi(emailA);


            clientResponse.put("reason", username + " - " + participant.getString("email_address") + " confirmed successfully");
            ResultSet rs = dbConnection.getSchool(participant.getString("registration_number"));
            if (rs.next()) {
            }
            String schoolName = rs.getString("name");
            Email email = new Email();
            email.sendParticipantConfirmedEmail(participant.getString("email_address"), participant.getString("username"), schoolName);
        } else {
            // handle when the command is 'no' for a participant
            dbConnection.createParticipantRejected(participant.getString("username"), participant.getString("firstname"), participant.getString("lastname"), participant.getString("email_address"), participant.getString("dob"), participant.getString("registration_number"));

            // delete the record from the local storage
            localStorage.deleteEntryByUserName(username);

            clientResponse.put("reason", username + " - " + participant.getString("email_address") + " rejected successfully");

            ResultSet rs = dbConnection.getSchool(participant.getString("registration_number"));
            if (rs.next()) {
            }

            String schoolName = rs.getString("name");

            // initialise email to send the participant rejected email
            Email email = new Email();
            email.sendParticipantRejectedEmail(participant.getString("email_address"), participant.getString("username"), schoolName);
        }
        clientResponse.put("status", true);
        return clientResponse;
    }

    private static void saveProfileImage(JSONObject s, String pic_path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ogenr\\Documents\\G-16-MATH-CHALLENGE-PROJECT\\web\\public\\assets\\participants\\" + pic_path)) {
            JSONArray arr = s.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                byte[] buffer = jsonArrayToBytes(o.getJSONArray("buffer"));
                fileOutputStream.write(buffer, 0, o.getInt("size"));
            }
            System.out.println("file saved as " + pic_path);
        } catch (Exception e) {
            System.out.println("Invalid image path provided");;
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

        // retrieve the applicants with the registration number of the representative
        String participants = localStorage.filterParticipantsByRegNo(regNo);

        JSONObject clientResponse = new JSONObject();
        clientResponse.put("command", "viewApplicants");
        clientResponse.put("applicants", participants);
        return clientResponse;
    }

    public JSONObject attempt(JSONObject obj) throws SQLException, ClassNotFoundException {
        JSONArray attempt = obj.getJSONArray("attempt");

        // create a connection with the server
        DbConnection dbConnection = new DbConnection();

        JSONObject attemptEvaluation = new JSONObject();

        // call the db connection and get the scores
        JSONObject resultObj = dbConnection.getAttemptScore(obj.getInt("challenge_id"), attempt, obj.getInt("participant_id"), obj.getInt("total_score"));
        attemptEvaluation.put("score", resultObj.getInt("score"));
        attemptEvaluation.put("participant_id", obj.getInt("participant_id"));
        attemptEvaluation.put("challenge_id", obj.getInt("challenge_id"));
        attemptEvaluation.put("total_score", obj.getInt("total_score"));

        // incase the challenge is not complete add it to the failed attempts table for incomplete challenges
        if (!obj.getBoolean("is_complete")) {
            dbConnection.create("INSERT INTO `failed_attempts` (`participant`) VALUES (" + obj.getInt("participant_id") + ");");
        }

        // regardless create an attempt on a challenge
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
                // call register logic
                return this.register(this.obj);
            case "viewChallenges":
                // call view logic
                return this.viewChallenges(this.obj);
            case "attemptChallenge":
                // call attempt logic
                return this.attemptChallenge(this.obj);
            case "confirm":
                // call confirm logic
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