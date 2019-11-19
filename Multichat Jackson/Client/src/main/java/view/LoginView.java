package view;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.AuthData;
import models.Payload;
import services.LoginService;
import services.TokenService;

import java.util.Scanner;

public class LoginView {
    public String openLoginPage() {
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
//            client.sendMessage(jsonValue);
            return jsonValue;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
