package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientInstance {
    // define attributes for the ClientInstance object
    String hostname;
    int port;

    public ClientInstance(String hostname, int port) {
        // constructor class for the client instance
        this.hostname = hostname;
        this.port = port;
    }

    public void start() throws IOException {
        // Todo: create a parent menu

        // execute code for interacting with the server
        try (
                Socket socket = new Socket(hostname, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        ) {
            System.out.println("Connection with server a success");

            // read command line input
            String userInput = consoleInput.readLine();

            // send content to the server
            output.println(userInput);

            // print server response
            String response = input.readLine();
            System.out.println("response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}