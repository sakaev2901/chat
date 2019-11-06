package servers;

import controllers.PayloadController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatMultiServer {
    private List<ClientHandler> clients;

    public ChatMultiServer() {
        clients = new CopyOnWriteArrayList<ClientHandler>(); // ????
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
                new ClientHandler(serverSocket.accept()).start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;

        ClientHandler(Socket socket) {
            this.clientSocket = socket;
            clients.add(this);
            System.out.println("New Client");
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                PayloadController payloadController = new PayloadController(clientSocket, clients, this);
                while (!clientSocket.isClosed() &&(inputLine = in.readLine()) != null) {
//                    if(".".equals(inputLine)) {
//                        for (ClientHandler client:
//                             clients) {
//                            PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream());
//                            out.println("bye");
//                        }
//                        break;
//                    } else  {
//                        for (ClientHandler client:
//                             clients) {
//                            PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream(), true);
//                            out.println(inputLine);
//                        }
//                    }
                    payloadController.handleRequest(inputLine);
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

        public void setClientSocket(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public BufferedReader getIn() {
            return in;
        }

        public void setIn(BufferedReader in) {
            this.in = in;
        }
    }
}
