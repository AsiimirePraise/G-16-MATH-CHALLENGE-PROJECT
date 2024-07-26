package client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.Duration;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.time.LocalDateTime.*;

public class ClientInstance {
    // define attributes for the ClientInstance object
    String hostname;
    int port;
    String clientId;
    User user;
    byte cache;
    boolean isStudent;
    boolean isAuthenticated;

    public ClientInstance(String hostname, int port, User user) {
        // constructor class for the client instance
        this.hostname = hostname;
        this.port = port;
        this.user = user;
    }

    public static boolean isValid(String input) {
        String regex = "^\\{.*\\}$";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return pattern.matcher(input).matches();
    }

    public JSONArray displayQuestionSet(JSONObject challengeObj) {
        // display the name of the challenge and the time
        System.out.println("====================================");
        System.out.println("       " + challengeObj.getString("challenge_name"));
        System.out.println("====================================");
        System.out.println("Challenge ID          : " + challengeObj.getInt("challenge_id"));
        System.out.println("Time allocation (mins): " + challengeObj.getInt("time_allocation"));
        System.out.println("====================================\n");

        // start a scanner obj
        Scanner scanner = new Scanner(System.in);

        // pick the questions from the challenge object from the response
        JSONArray questions = challengeObj.getJSONArray("questions");

        // create an array for collecting answers
        JSONArray solutions = new JSONArray();
        this.cache = 0;
        int count = 1;
        int numberOfQuestions = questions.length();

        // meta data for tracking time
        int timeAllocation = challengeObj.getInt("time_allocation");
        LocalTime startingTime = LocalTime.now();
        LocalTime closingTime = startingTime.plusMinutes(timeAllocation);
        LocalTime lastTime = startingTime;

        for (int i = 0; i < questions.length(); i++) {
            // retrieve a particular question
            JSONObject question = questions.getJSONObject(i);

            // create a json object for picking answers
            JSONObject answer = new JSONObject();

            // add my score to the temporary storage - cache
            this.cache += (byte) question.getInt("score");

            // print out question
            Duration remainingTime = Duration.between(startingTime, LocalTime.now());
            System.out.println("done: " + solutions.length() + "/" + numberOfQuestions + "             time-left: " + ((timeAllocation - 1) - remainingTime.toMinutes()) + " minutes " + (60 - (remainingTime.toSeconds() % 60)) + " seconds");
            System.out.println("question: " + question.getString("question") + " (" + question.getInt("score") + " Marks)");
            System.out.print("answer  : ");

            // append the answers to the solutions array
            answer.put("question_id", question.getInt("id"));
            answer.put("answer", scanner.nextLine());
            answer.put("time", (int) Duration.between(lastTime, LocalTime.now()).getSeconds());

            lastTime = LocalTime.now();
            System.out.println("-----------------------------------------------------\n");
            if (closingTime.isBefore(LocalTime.now())) {
                // stop attempting if the duration of attempt is closed
                return solutions;
            }

            // add answer json object to the solutions array
            solutions.put(answer);
            count++;


            System.out.print("\n");
        }

        // return solutions - questions & answers
        return solutions;
    }

    public void start() throws IOException {
        // Todo: create a parent menu
        // execute code for interacting with the server
        try (
                Socket socket = new Socket(hostname, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            this.clientId = socket.getInetAddress().getHostAddress();
            Serializer serializer = new Serializer(this.user);
            ClientController clientController = new ClientController(user);

            printMenu();

            System.out.print("[" + this.clientId + "] (" + this.user.username + ") -> ");
            // read command line input
            // Continuously read from the console and send to the server

            String regex = "^\\{.*\\}$";
            Pattern pattern = Pattern.compile(regex);
            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                // send command to the server
                if (userInput.equals("logout") && (this.user.isAuthenticated)) {
                    System.out.println("Session successfully logged out");
                    this.user.logout();
                    System.out.print("[" + this.clientId + "] (" + (!this.user.username.isBlank() ? this.user.username : null) + ") -> ");
                    continue;
                }

                // convert command into a json string
                String serializedCommand = serializer.serialize(userInput);

                if (isValid(serializedCommand)) {
                    // send the json object to the server
                    output.println(serializedCommand);

                    // read response here from the server
                    String response = input.readLine();

                    // interpret response with client controller - logic for interpreting client response
                    this.user = clientController.exec(response);

                    //
                    if (!pattern.matcher(this.user.output).matches()) {
                        // print response if it's a plain string like if its login
                        System.out.println("\n" + user.output + "\n");
                    } else {
                        // handle for example collecting answers to questions

                        //create my json object for holding the solutions
                        JSONObject questions = new JSONObject(this.user.output);

                        // collect answers using the display question set function
                        JSONArray answerSet = displayQuestionSet(questions);

                        // create data to send to the server
                        JSONObject obj = new JSONObject();
                        obj.put("attempt", answerSet);
                        obj.put("participant_id", this.user.id);
                        obj.put("command", "attempt");
                        obj.put("challenge_id", questions.getInt("challenge_id"));
                        obj.put("total_score", this.cache);
                        obj.put("is_complete", (questions.length() == answerSet.length()));

                        String inp = obj.toString();

                        // send that data to the server
                        output.println(inp);

                        // read server response
                        response = input.readLine();

                        // inteprete server response in the client controller
                        this.user = clientController.exec(response);

                        System.out.println("\n" + user.output + "\n");
                    }
                } else {
                    System.out.println(serializedCommand);
                }
                // prompt for the next instruction
                System.out.print("[" + this.clientId + "] (" + this.user.username + ") -> ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connection with the server timeout");
        }
    }

    private void printMenu() {
        System.out.println("===================================");
        System.out.println("        MATH CHALLENGE MENU        ");
        System.out.println("===================================");
        System.out.println("\nWelcome to the MATH CHALLENGE CLI. Use the menu below to guide you as you use this client\n");
        System.out.println("1. Register [to register as a participant in the different challenges]:");
        System.out.println("   register <username> <firstname> <lastname> <email> <date_of_birth> <school_registration_number> <path_to_image>");
        System.out.println("\n2. View Challenges [to view the different open challenges in the system]:");
        System.out.println("   viewChallenges");
        System.out.println("\n3. Attempt Challenge [to attempt a particular open challenge]:");
        System.out.println("   attemptChallenge <challenge_id>");
        System.out.println("\n4. View Applicants [for representatives to view registering participants]:");
        System.out.println("   viewApplicants");
        System.out.println("\n5. Confirm Applicant [for representatives to confirm participant registration]:");
        System.out.println("   confirm <yes/no> <applicant_username>");
        System.out.println("\n6. Log in [to login as participant or as a representative]:");
        System.out.println("   login");
        System.out.println("\n7. Log out [to logout as participant or as a representative]:");
        System.out.println("   logout");
        System.out.println("\n===================================\n");
    }
}