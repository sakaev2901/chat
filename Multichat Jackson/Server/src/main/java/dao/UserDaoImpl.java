package dao;

import config.ConnectionConfig;
import models.AuthData;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDaoImpl implements UserDao<User> {

    private final ConnectionConfig CONFIG =ConnectionConfig.getInstance();
    private final String FIND_BY_MAIL_AND_PASSWORD = "SELECT * FROM chat_user WHERE \"mail\"=?";
    private final String SAVE = "INSERT INTO chat_user (name, mail, password) VALUES (?, ?, ?)";

    public User findByMailAndPassword(String mail, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        User user = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_BY_MAIL_AND_PASSWORD);
            statement.setString(1, mail);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                String hashPassword = set.getString("password");
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                if (!encoder.matches(password, hashPassword)) {
                    return user;
                }
                user = new User();
                AuthData authData = new AuthData();
                authData.setMail(set.getString("mail"));
                user.setAuthData(authData);
                user.setName(set.getString("name"));
                user.setId(set.getInt("id"));
                user.setRole(set.getString("role"));
            }
            return user;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

    public void save(User model) {
        Connection connection = null;
        PreparedStatement statement = null;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(SAVE);
            statement.setString(1, model.getName());
            statement.setString(2, model.getAuthData().getMail());
            String hashPassword = encoder.encode(model.getAuthData().getPassword());
            statement.setString(3, hashPassword);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }
}
