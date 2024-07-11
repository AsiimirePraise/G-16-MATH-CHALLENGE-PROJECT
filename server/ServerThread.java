package server;

import java.net.*;


public class ServerThread {
    private Socket socket;
    // server thread properties

    public ServerThread(Socket socket) {
        this.socket = socket;
        // define constructor for the Server thread
    }

    public void start() {
        System.out.println("Thread started");
        // start a thread for communicating with the client continously
    }
}