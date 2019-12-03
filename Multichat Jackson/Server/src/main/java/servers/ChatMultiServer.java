package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatMultiServer {
    private List<ClientHandler> clients;

    public ChatMultiServer() {
        clients = new CopyOnWriteArrayList<>();
    }

    public void start(int port) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        while (true) {
            try {
                new ClientHandler(serverSocket.accept(), this).start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public List<ClientHandler> getClients() {
        return clients;
    }
}
