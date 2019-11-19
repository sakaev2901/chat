package controllers;

import clients.SocketClient;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import models.Payload;
import net.Session;
import services.*;
import view.AddingToCartView;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MessageResolver {

    SocketClient client;

    public MessageResolver() {

    }

    public MessageResolver(SocketClient client) {
        this.client = client;
        new LoginService(this.client).doLogin();
    }

    public void resolve(String serverMessage) {
        MenuService menuService = new MenuService(client);
        ObjectMapper objectMapper = new ObjectMapper();
        Payload<Object> data = null;
        JavaType type = objectMapper.getTypeFactory().constructParametricType(Payload.class, Object.class);
        Session session = Session.getInstance();
        try {
            data = objectMapper.readValue(serverMessage, type);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        String header = data.getHeader();
        switch (header) {
            case "Message": {
                type = objectMapper.getTypeFactory().constructParametricType(Payload.class, Message.class);
                try {
                    Payload<Message> payloadOfMessage = objectMapper.readValue(serverMessage, type);
                    Message message = payloadOfMessage.getPayload();
                    System.out.println(message.getTimeStamp() + " <"+message.getSenderName()+">: " + message.getText());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            break;
            case "messageArchive": {
                type = objectMapper.getTypeFactory().constructParametricType(Payload.class, LinkedList.class);
                try {
                    Payload<LinkedList<LinkedHashMap<String, String>>> payloadOfArchive = objectMapper.readValue(serverMessage, type);
                    LinkedList<LinkedHashMap<String, String>> messageList = payloadOfArchive.getPayload();
                    new MessageArchiveService(client).receiveArchive(messageList);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            break;
            case "Login": {
                TokenService tokenService = new TokenService();
                type = objectMapper.getTypeFactory().constructParametricType(Payload.class, Boolean.class);
                try {
                    Payload<Boolean> payloadOfLoginStatus = objectMapper.readValue(serverMessage, type);
                    Boolean loginStatus = payloadOfLoginStatus.getPayload();
                    if (loginStatus) {
                        menuService.receiveMenu();
                    } else {
                        tokenService.deleteToken();
                        System.err.println("Wrong login or password");
                        new LoginService(client).doLogin();
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            break;
            case "logout": {

            }
            break;
            case "Command": {
                LinkedHashMap<String, String> commandPayload = (LinkedHashMap<String, String>)data.getPayload();
                String command = commandPayload.get("command");
                switch (command) {
                    case "get products": {
                        new ShopService(client).receiveProducts(serverMessage);
                        if (session.getRole().equals("user")) {
                            AddProductToCartService addProductToCartService = new AddProductToCartService(client);
                            while (addProductToCartService.addToCart()) {

                            }
                            menuService.receiveMenu();
                        } else {
                            menuService.receiveMenu();
                        }
                    }
                    break;
                    case "get cart": {
                        new CartService(client).setCart(serverMessage);
                    }
                }
            }
            break;
            case "Token": {
                new TokenService().saveToken(serverMessage);
            }
            break;

        }
    }
}
