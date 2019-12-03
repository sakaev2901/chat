package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Payload;
import models.User;
import servers.ChatMultiServer;
import services.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
            CartServiceImpl cartService = new CartServiceImpl();
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
                            ProductsServiceImpl productsService = new ProductsServiceImpl();
                            out.println(productsService.getProducts());
                        }
                        break;
                        case "set product": {
                            ProductsServiceImpl productsService = new ProductsServiceImpl();
                            productsService.addProduct(jsonRequest);
                        }
                        break;
                        case "set product to cart": {
                            cartService.addToCart(jsonRequest, this.user.getId());
                        }
                        break;
                        case "get cart": {
                            out.println(cartService.getCart(jsonRequest));
                        }
                        break;
                        case "buy cart": {
                            cartService.buyCart(user.getId());
                        }
                        break;
                        case "clear cart": {
                            cartService.clearCart(user.getId());
                        }
                        break;
                        case "get orders": {
                            OrderServiceImpl orderService = new OrderServiceImpl();
                            out.println(orderService.getOrders(user.getId()));
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
        LoginServiceImpl loginService = new LoginServiceImpl(clientSocket);
        this.user = loginService.login((LinkedHashMap<String, String>)payload.getPayload());
    }

    public void doMessage(Payload payload) {
        MessageServiceImpl messageService = new MessageServiceImpl();
        messageService.sendMessage((LinkedHashMap<String, String>)payload.getPayload(), clients, this.user);
    }

    public void getMessages(LinkedHashMap commandPayload) {
        PaginationServiceImpl paginationService = new PaginationServiceImpl();
        Integer page = Integer.parseInt((String) commandPayload.get("page"));
        Integer size = Integer.parseInt((String) commandPayload.get("size"));
        out.println(paginationService.getMessages(page, size));
    }
}
