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
    String clientId;

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
            this.clientId = (String) socket.getInetAddress().getHostAddress();
            Serializer serializer = new Serializer(false);
            System.out.println("Connection with server a success");
            System.out.print("[" + this.clientId + "] -> ");
            // read command line input
            // Continuously read from the console and send to the server
            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                // send command to the server
                String serializedCommand = serializer.serialize(userInput);
                if (serializedCommand.equals("Invalid command")) {
                    System.out.println(serializedCommand);

                } else {
                    output.println(serializedCommand);

                    // read response here from the server
                    String response = input.readLine();
                    System.out.println("response: " + response);
                }


                // prompt for the next instruction
                System.out.print("[" + this.clientId + "] -> ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}