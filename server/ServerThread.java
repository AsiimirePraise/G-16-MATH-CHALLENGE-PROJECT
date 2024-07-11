package server;

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

    public void start() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ){

            // read input from the client
            String clientInput = input.readLine();
            System.out.println("server received: " + clientInput);

            // send content back to client
            output.println("Server received -> " + clientInput);


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Thread started");

        // start a thread for communicating with the client continously
    }
}