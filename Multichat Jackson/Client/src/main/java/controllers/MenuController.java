package controllers;

import clients.SocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import models.AuthData;
import models.Message;
import models.Payload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MenuController {

    private SocketClient client;

    public void openWelcomeMenu() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== MultiChat V-1.0 ===");
            System.out.println("1. Login");
            System.out.println("2. Registration");
            int button = scanner.nextInt();
            if (button == 1) {
                openLoginPage();
                break;
            } else if (button == 2) {
                openRegistrationPage();
                break;
            } else {
                System.err.println("Wrong button");
            }
        }

    }

    public void handleResponse(String response) {
        switch (response) {
            case "Wrong password or login": {
                System.out.println(response);
                openLoginPage();
            }
            break;
            default: {
                if (response.contains("Hello,")) {
                    System.out.println(response);
                    openUserMenu();
                } else {
                    System.out.println(response);
                }
            }
        }
    }

    public void openLoginPage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Login ===");
        System.out.print("Mail: ");
        String mail = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        AuthData authData = new AuthData();
        authData.setMail(mail);
        authData.setPassword(password);
        Payload payload = new Payload("Login", authData);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonValue = objectMapper.writeValueAsString(payload);
            client.sendMessage(jsonValue);
            String status = client.getIn().readLine();
            if (status.equals("false")) {
                openLoginPage();
            } else {
                openUserMenu();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void openRegistrationPage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Registration ===");
    }

    public void openUserMenu() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== MENU ===");
            System.out.println("1. Go to the chat");
            System.out.println("2. Message archive");
            System.out.println("3. Logout");
            Integer button = scanner.nextInt();
            if (button == 1) {
                System.out.println("=== CHAT ===");
                openChat();
                break;
            } else if(button == 2) {
                openMessageArchive();
            }
            else if (button == 3) {
                logout();
                openLoginPage();
            } else {
                System.out.println("Wrong button");
            }
        }
    }

    private void openMessageArchive() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Message Archive ===");
        System.out.print("Size: ");
        Integer size = scanner.nextInt();
        System.out.print("Page: ");
        Integer page = scanner.nextInt();
    }

    public void openChat() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String messageText = scanner.nextLine();
            if (messageText.equals("$menu")) {
                openUserMenu();
                break;
            }
            Message message = new Message(messageText);
            Payload<Message> payload = new Payload("Message", message);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonValue = mapper.writeValueAsString(payload);
                client.sendMessage(jsonValue);
                System.out.println(client.getIn().readLine());
            } catch (Exception e) {
                throw new IllegalStateException(e);

            }
        }
    }

    public void logout() {
        Payload payload = new Payload();
        payload.setHeader("Logout");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonObject = mapper.writeValueAsString(payload);
            client.sendMessage(jsonObject);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public MenuController(SocketClient client) {
        this.client = client;
    }
}
