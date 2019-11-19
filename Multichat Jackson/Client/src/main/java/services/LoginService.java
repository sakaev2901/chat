package services;

import clients.SocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.AuthData;
import models.Payload;
import view.LoginView;

import java.net.Socket;

public class LoginService {
    SocketClient client;

    public LoginService(SocketClient client) {
        this.client = client;
    }

    public void doLogin() {
        TokenService tokenService = new TokenService();
        String token = tokenService.getToken();
        if (token != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Payload authdata = new Payload<>();
            AuthData authData = new AuthData();
            authData.setToken(token);
            authdata.setPayload(authData);
            authdata.setHeader("Login");
            try {
                String jsonAuthdata = objectMapper.writeValueAsString(authdata);
                client.sendMessage(jsonAuthdata);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        } else {
            client.sendMessage(new LoginView().openLoginPage());
        }
    }
}
