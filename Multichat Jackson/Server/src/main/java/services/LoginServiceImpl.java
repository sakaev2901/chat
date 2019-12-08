package services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import context.Component;
import protocol.Request;
import repositories.UsersRepository;
import repositories.UsersRepositoryImpl;
import models.Payload;
import models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;

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
            user = usersRepository.findByMailAndPassword(mail, password);

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

    public void setUsersRepository(UsersRepositoryImpl usersRepository) {
        this.usersRepository = usersRepository;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public String getComponentName() {
        return null;
    }
}
