package services;

import models.User;
import protocol.Request;

import java.util.LinkedHashMap;

public interface LoginService {
    User login(Request request);

}
