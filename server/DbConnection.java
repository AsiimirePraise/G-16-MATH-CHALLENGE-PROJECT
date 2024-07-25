package server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class DbConnection {
    // database connection parameters
    String url = "jdbc:mysql://localhost:3306/mtchallenge";
    String username = "root";
    String password = "root";
    Connection connection;
    Statement statement;

    public DbConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        this.statement = connection.createStatement();
    }

    public void create(String sqlCommand) throws SQLException {
        this.statement.execute(sqlCommand);
    }

    public ResultSet read(String sqlCommand) throws SQLException {
        return this.statement.executeQuery(sqlCommand);
    }

    public void update(String sqlCommand) throws SQLException {
        this.statement.execute(sqlCommand);
    }

    public void delete(String sqlCommand) throws SQLException {
        this.statement.execute(sqlCommand);
    }

    public void close() throws SQLException {
        if (this.statement != null) this.statement.close();
        if (this.connection != null) this.connection.close();
    }

    public void createParticipant(String username, String firstname, String lastname, String emailAddress, String dob, String regNo, String imagePath) throws SQLException {
        String sql = "INSERT INTO `participants` (`username`, `firstname`, `lastname`, `email_address`, `dob`, `registration_number`, `image_path`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, firstname);
            stmt.setString(3, lastname);
            stmt.setString(4, emailAddress);
            stmt.setString(5, dob);
            stmt.setString(6, regNo);
            stmt.setString(7, imagePath);
            stmt.executeUpdate();
        }
    }

    public void createParticipantRejected(String username, String firstname, String lastname, String emailAddress, String dob, String regNo) throws SQLException {
        String sql = "INSERT INTO `rejected_participants` (`username`, `firstname`, `lastname`, `email_address`, `dob`, `registration_number`) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, firstname);
            stmt.setString(3, lastname);
            stmt.setString(4, emailAddress);
            stmt.setString(5, dob);
            stmt.setString(6, regNo);
            stmt.executeUpdate();
        }
    }

    public ResultSet getChallenges() throws SQLException {
        String sql = "SELECT * FROM `mtchallenge`.`challenges` WHERE `start_date` <= CURRENT_DATE AND `closing_date` >= CURRENT_DATE;";
        return this.statement.executeQuery(sql);
    }

    public ResultSet getChallengeQuestions(int challenge_id) throws SQLException {
        String sql = "SELECT qa.* FROM `question_answers` qa JOIN `challenge_question_answers` cqa ON qa.id = cqa.question WHERE cqa.challenge = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, challenge_id);
        return preparedStatement.executeQuery();
    }

    public JSONObject getAttemptScore(int challenge, JSONArray attempt, int participant, int total) throws SQLException {
        int score = 0;
        StringBuilder performance = new StringBuilder();
        int totalTime = 0;

        performance.append("Thank you for participating in this challenge\n");
        performance.append("\nBelow is your performance in the challenge for each question:\n");
        performance.append("-----------------------------------------------------------\n\n");

        for (int i = 0; i < attempt.length(); i++) {
            JSONObject obj = attempt.getJSONObject(i);
            performance.append("Question ").append(i + 1).append(":\t");

            if (obj.get("answer").equals("-")) {
                score += 0;
                this.addAttempt(challenge, participant, obj.getInt("question_id"), false);
                performance.append("Score: ").append(0).append("\t");
                performance.append("Duration: ").append(String.format("%02d:%02d", obj.getInt("time") / 60, obj.getInt("time") % 60)).append("\n");
                continue;
            }

            String sql = "SELECT `score` FROM `question_answers` WHERE `id` = " + obj.getInt("question_id") + " AND `answer` = '" + obj.getString("answer") + "';";
            ResultSet questionScore = this.statement.executeQuery(sql);

            if (questionScore.next()) {
                score += questionScore.getInt("score");
                performance.append("Score: ").append(questionScore.getInt("score")).append("\t");
                this.addAttempt(challenge, participant, obj.getInt("question_id"), true);
            } else {
                score -= 3;
                performance.append("Score: ").append(-3).append("\t");
                this.addAttempt(challenge, participant, obj.getInt("question_id"), false);
            }
            performance.append("Duration: ").append(String.format("%02d:%02d", obj.getInt("time") / 60, obj.getInt("time") % 60)).append("\n");
            totalTime += obj.getInt("time");
        }

        performance.append("\nOverall Performance:\n");
        performance.append("-----------------------------------------------------------\n");
        performance.append("Final Score: ").append(Math.round(((float) score / (float) total) * 100)).append("\n");
        performance.append("Total Time: ").append(String.format("%02d:%02d", totalTime / 60, totalTime % 60)).append("\n");
        performance.append("-----------------------------------------------------------\n");

        JSONObject data = new JSONObject();
        data.put("results", performance.toString());

        data.put("score", score);


        return data;
    }

    public void createChallengeAttempt(JSONObject obj) throws SQLException {
        String sql = "INSERT INTO `participant_challenges` (`participant`, `challenge`, `score`, `total`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, obj.getInt("participant_id"));
            ps.setInt(2, obj.getInt("challenge_id"));
            ps.setInt(3, obj.getInt("score"));
            ps.setInt(4, obj.getInt("total_score"));
            ps.executeUpdate();
        }
        ;
    }

    public ResultSet getRepresentative(String regNo) throws SQLException {
        String sqlCommand = "SELECT * FROM `schools` WHERE registration_number = " + regNo + ";";
        return this.statement.executeQuery(sqlCommand);
    }

    public ResultSet getSchool(String regNo) throws SQLException {
        String sqlCommand = "SELECT name FROM `schools` WHERE registration_number = " + regNo + ";";
        return this.statement.executeQuery(sqlCommand);
    }

    public void addAttempt(int challenge, int participant, int question, boolean status) throws SQLException {
        String sqlCommand = "INSERT INTO `attempts` (`status`, `question`, `participant`, `challenge`) VALUES (" + (status ? "'correct'" : "'wrong'") + ", " + question + ", " + participant + ", " + challenge + ");";
        this.statement.execute(sqlCommand);
    }

    public ResultSet selectEmail(String regNo) throws SQLException {
        String sqlQ = "SELECT email_address FROM `participants` WHERE registration_number = \"" + regNo + "\";";
        return this.statement.executeQuery(sqlQ);
    }
}