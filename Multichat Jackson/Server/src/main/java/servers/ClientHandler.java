package servers;

import protocol.MessageResolver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private MessageResolver messageResolver;
    private List<ClientHandler> clients;

    ClientHandler(Socket socket, ChatMultiServer chatMultiServer) {
        this.clientSocket = socket;
        clients = chatMultiServer.getClients();
        clients.add(this);
        System.out.println("New Client");
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            this.messageResolver = new MessageResolver(clientSocket, clients, this);
            while (!clientSocket.isClosed() &&(inputLine = in.readLine()) != null) {
                messageResolver.handleRequest(inputLine);
            }
            in.close();
            clientSocket.close();
            clients.remove(this);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}