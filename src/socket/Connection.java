package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
    ServerSocket serverSocket;
    Socket socket;

    public Connection(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port " + port);
            while (true) {
                socket = serverSocket.accept();
                Server server = new Server(socket);
                server.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection server = new Connection(2000);
    }
}
