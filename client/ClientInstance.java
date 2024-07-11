package client;

import java.io.IOException;
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
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connection with server a success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}