package services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import context.Component;
import protocol.Request;
import repositories.UsersRepository;

import models.Payload;
import models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Optional;

public class LoginServiceImpl implements LoginService, Component {


    private UsersRepository usersRepository;
    private TokenService tokenService;

    public LoginServiceImpl() {

    }

    public User login(Request request) {
        User user = null;
        if (request.getParameter("token") != null) {
            String userToken = request.getParameter("token");
            user = tokenService.parse(userToken);
        } else {
            String mail = request.getParameter("mail");
            String password = request.getParameter("password");
            Optional<User> optionalUser = usersRepository.findByMailAndPassword(mail, password);
            user = optionalUser.isPresent() ? optionalUser.get() : new User();
        }
        if (user.getId() != null) {
            try {
                user.setToken(tokenService.getToken(user.getId(), user.getRole()));
            } catch ( JOSEException e) {
                throw new IllegalStateException(e);
            }
            System.out.println(user.getId() + " connected");
        }
        return user;
    }


    public UsersRepository getUsersRepository() {
        return usersRepository;
    }



    public TokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public String getComponentName() {
        return "loginServiceImpl";
    }
}
