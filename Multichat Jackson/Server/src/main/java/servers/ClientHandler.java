package servers;

import protocol.MessageResolver;
import protocol.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private MessageResolver messageResolver;
    private List<ClientHandler> clients;
    private ChatMultiServer server;
    private PrintWriter out;

    ClientHandler(Socket socket, ChatMultiServer chatMultiServer) {
        this.server = chatMultiServer;
        this.clientSocket = socket;
        clients = chatMultiServer.getClients();
        clients.add(this);
        System.out.println("New Client");
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            this.messageResolver = new MessageResolver(this);
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

    public void sendMessage(Response response) {
        out.println(response.toJson());
    }

    private void sendMessageAllClient() {

    }
    public Socket getClientSocket() {
        return clientSocket;
    }
}