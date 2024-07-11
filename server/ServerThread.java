package server;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class ServerThread {
    private Socket socket;
    // server thread properties
    public ServerThread(Socket socket) {
        this.socket = socket;
        // define constructor for the Server thread
    }
    public JSONObject readUserInput(BufferedReader input) throws IOException {
        String clientInput;
        StringBuilder clientIn = new StringBuilder();
        // iterate until the end of the json data
        while ((clientInput = input.readLine()) != null) {
            clientIn.append(clientInput);
            if (clientInput.equals("}")) {
                break;
            }
        }
        // load data into a json format
        JSONObject jsonObject = new JSONObject(clientIn.toString());
        return jsonObject;
    }
    public void start() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
                ){

            // read user input
            JSONObject clientRequest;
            while ((clientRequest = this.readUserInput(input)) != null) {
                System.out.println(clientRequest.get("type") + "\n" + clientRequest.toString());
                System.out.println("server received: " + clientRequest.toString());

                // send content back to client
                output.println("Server received -> " + clientRequest.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Thread started");
        // start a thread for communicating with the client continously
    }
}