package services;

import models.User;

import java.util.LinkedHashMap;

public interface LoginService {
    User login(LinkedHashMap<String, String> authData);

}
