package dao;

import models.User;

public interface UserDao<T> extends BaseDao<T> {
    User findByMailAndPassword(String mail, String password);
}
