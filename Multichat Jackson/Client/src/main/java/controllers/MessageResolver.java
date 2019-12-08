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
import java.util.*;

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
//                type = objectMapper.getTypeFactory().constructParametricType(Payload.class, LinkedList.class);
                try {
                    Payload payloadOfArchive = objectMapper.readValue(serverMessage, Payload.class);
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)payloadOfArchive.getPayload();
                    ArrayList<LinkedHashMap<String, String>> messageList = (ArrayList<LinkedHashMap<String, String>>)map.get("messages");
                    new MessageArchiveService(client).receiveArchive(messageList);
                    System.out.println(payloadOfArchive);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            break;
            case "Login": {
                TokenService tokenService = new TokenService();
                try {
                    Payload payloadOfLoginStatus = objectMapper.readValue(serverMessage, Payload.class);
                    HashMap<String, String> map = (HashMap<String, String>)payloadOfLoginStatus.getPayload();
                    if (!(map.get("id") == null)) {
                        tokenService.saveToken(map.get("token"));
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
                break;
            }
            case "get cart": {
                new CartService(client).setCart(serverMessage);
            }
            break;
            case "get orders": {
                OrderService orderService = new OrderService(client);
                orderService.setOrders(serverMessage);
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                menuService.receiveMenu();
            } break;
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
                    break;
                    case "get orders": {
                        OrderService orderService = new OrderService(client);
                        orderService.setOrders(serverMessage);
                        Scanner scanner = new Scanner(System.in);
                        scanner.nextLine();
                        menuService.receiveMenu();
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
