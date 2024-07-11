package client;

import java.io.IOException;

public class Client {
    // define hostname and port number
    String hostname;
    int port;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public ClientInstance startClientInstance() throws IOException {
        ClientInstance clientInstance = new ClientInstance(hostname, port);
        clientInstance.start();
        return clientInstance;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8080);

        // run client instance
        // create a new client instance
        client.startClientInstance();
    }
}