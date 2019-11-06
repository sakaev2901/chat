package services;


import dao.UserDaoImpl;
import models.User;

import java.util.LinkedHashMap;

public class LoginService {
    public User login(LinkedHashMap<String, String> authData) {
        String mail = authData.get("mail");
        String password = authData.get("password");
        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.findByMailAndPassword(mail, password);
        return user;
    }
}
