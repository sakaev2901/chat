package servers;

import context.Component;
import protocol.MessageResolver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatMultiServer implements Component {
    private MessageResolver messageResolver;
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

    @Override
    public String getComponentName() {
        return null;
    }

    public MessageResolver getMessageResolver() {
        return messageResolver;
    }

    public void setMessageResolver(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    public void setClients(List<ClientHandler> clients) {
        this.clients = clients;
    }
}
