package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Message;
import models.Payload;
import models.User;
import servers.ChatMultiServer;
import services.LoginService;
import services.MessageService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.List;

public class PayloadController {

    private Socket clientSocket;
    private PrintWriter out;
    private User user;
    private List<ChatMultiServer.ClientHandler> clients;
    private ChatMultiServer.ClientHandler client;

    public PayloadController(Socket clientSocket, List<ChatMultiServer.ClientHandler> clients, ChatMultiServer.ClientHandler client) {
        try {
            this.clientSocket = clientSocket;
            this.clients = clients;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.client = client;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void handleRequest(String jsonRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Payload payload = objectMapper.readValue(jsonRequest, Payload.class);
            String header = payload.getHeader();
            switch (header) {
                case "Login": {
                    doLogin(payload);
                }
                break;
                case "Logout": {
                    doLogout(payload);
                }
                break;
                case "Message": {
                    doMessage(payload);
                }
                break;
                case "Command": {

                }
                break;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void doLogout(Payload payload) {
        clients.remove(client);
        System.out.println(user.getId() + " disconnected");
    }

    public void doLogin(Payload payload) {
        LoginService loginService = new LoginService();
        this.user = loginService.login((LinkedHashMap<String, String>)payload.getPayload());
        if (user != null) {
            out.println("Hello, " + user.getName());
            out.println("true");
            System.out.println(user.getId() + " connected");
        } else {
            out.println("Wrong password or login");
            out.println("false");
        }
    }

    public void doMessage(Payload payload) {
        MessageService messageService = new MessageService();
        messageService.sendMessage((LinkedHashMap<String, String>)payload.getPayload(), clients, user);
    }
}
