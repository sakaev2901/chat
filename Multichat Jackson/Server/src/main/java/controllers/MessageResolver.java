package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.shape.StrokeLineCap;
import models.Message;
import models.Payload;
import models.Product;
import models.User;
import org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparations;
import servers.ChatMultiServer;
import services.LoginService;
import services.MessageService;
import services.PaginationService;
import services.ProductsService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.List;

public class MessageResolver {

    private Socket clientSocket;
    private PrintWriter out;
    private User user;
    private List<ChatMultiServer.ClientHandler> clients;
    private ChatMultiServer.ClientHandler client;

    public MessageResolver(Socket clientSocket, List<ChatMultiServer.ClientHandler> clients, ChatMultiServer.ClientHandler client) {
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
                    LinkedHashMap<String, String> commandPayload = (LinkedHashMap<String, String>)payload.getPayload();
                    String command = commandPayload.get("command");
                    switch (command) {
                        case "get messages": {
                            getMessages(commandPayload);
                        }
                        break;
                        case "get products": {
                            ProductsService productsService = new ProductsService();
                            out.println(productsService.getProducts());
                        }
                        break;
                        case "set product": {
                            ProductsService productsService = new ProductsService();
                            productsService.addProduct(jsonRequest);
                        }
                    }
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
        LoginService loginService = new LoginService(clientSocket);
        this.user = loginService.login((LinkedHashMap<String, String>)payload.getPayload());
    }

    public void doMessage(Payload payload) {
        MessageService messageService = new MessageService();
        messageService.sendMessage((LinkedHashMap<String, String>)payload.getPayload(), clients, this.user);
    }

    public void getMessages(LinkedHashMap commandPayload) {
        PaginationService paginationService = new PaginationService();
        Integer page = Integer.parseInt((String) commandPayload.get("page"));
        Integer size = Integer.parseInt((String) commandPayload.get("size"));
        out.println(paginationService.getMessages(page, size));
    }
}
