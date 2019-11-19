package services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import dao.UserDaoImpl;
import models.Payload;
import models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;

public class LoginService {

    Socket client;

    public LoginService(Socket client) {
        this.client = client;
    }

    public User login(LinkedHashMap<String, String> authData) {
        String mail = authData.get("mail");
        String password = authData.get("password");
        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.findByMailAndPassword(mail, password);
        PrintWriter out = null;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        Payload<Boolean> loginPayload = new Payload();
        loginPayload.setHeader("Login");
        String loginStatus;
        if (user != null) {
            loginPayload.setPayload(true);
            try {
                loginStatus = new ObjectMapper().writeValueAsString(loginPayload);
                Payload<String> tokenPayload = new Payload<>();
                tokenPayload.setHeader("Token");
                tokenPayload.setPayload(new TokenService().getToken(user.getId(), user.getRole()));
                String token = new ObjectMapper().writeValueAsString(tokenPayload);
                out.println(token);
                out.println(loginStatus);
            } catch (JsonProcessingException | JOSEException e) {
                throw new IllegalStateException(e);
            }
            System.out.println(user.getId() + " connected");
        } else {
            loginPayload.setPayload(false);
            try {
                loginStatus = new ObjectMapper().writeValueAsString(loginPayload);
                out.println(loginStatus);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }

        }
        return user;
    }
}
