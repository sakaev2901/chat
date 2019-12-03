package services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import repositories.UsersRepository;
import repositories.UsersRepositoryImpl;
import models.Payload;
import models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;

public class LoginServiceImpl implements LoginService{

    Socket client;

    public LoginServiceImpl(Socket client) {
        this.client = client;
    }

    public User login(LinkedHashMap<String, String> authData) {
        UsersRepository usersRepository = new UsersRepositoryImpl();
        User user = null;
        if (authData.get("token") != null) {
            String userToken = authData.get("token");
            TokenServiceImpl tokenService = new TokenServiceImpl();
            user = tokenService.parse(userToken);
        } else {
            String mail = authData.get("mail");
            String password = authData.get("password");
            user = usersRepository.findByMailAndPassword(mail, password);

        }
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
                tokenPayload.setPayload(new TokenServiceImpl().getToken(user.getId(), user.getRole()));
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