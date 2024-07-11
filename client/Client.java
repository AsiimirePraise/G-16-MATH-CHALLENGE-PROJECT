package client;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        // define hostname and port number
        String hostname = "localhost";
        int port = 8080;

        // create a new client instance
        ClientInstance clientInstance = new ClientInstance(hostname, port);
        clientInstance.start();

        // run client instance
    }
}