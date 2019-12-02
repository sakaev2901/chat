package repositories;

import config.ConnectionConfig;
import models.AuthData;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class UsersRepositoryImpl implements UsersRepository<User> {

    private final ConnectionConfig CONFIG =ConnectionConfig.getInstance();
    private final String FIND_BY_MAIL_AND_PASSWORD = "SELECT * FROM chat_user WHERE \"mail\"=?";
    private final String SAVE = "INSERT INTO chat_user (name, mail, password) VALUES (?, ?, ?)";
    private final String UPDATE_VERIFIER = "UPDATE chat_user SET \"verifier\" = ? WHERE \"id\" = ?";
    private final String FIND_VERIFIER_BY_ID = "SELECT verifier FROM chat_user WHERE \"id\" = ?";
    private final String FIND_BY_ID = "SELECT * FROM chat_user WHERE \"id\" = ?";


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

    @Override
    public List<User> findAll() {
        return null;
    }


    public void updateVerifier(String verifier, Integer id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(UPDATE_VERIFIER);
            statement.setString(1, verifier);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

    public String findVerifierById(Integer id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String verifier = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_VERIFIER_BY_ID);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                verifier = set.getString("verifier");
            }
            return verifier;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

    public Optional<User> findById(Integer id) {
        Connection connection = null;
        PreparedStatement statement = null;
        User user = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_BY_ID);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                user = new User();
                user.setId(set.getInt("id"));
                user.setRole(set.getString("role"));
                user.setName(set.getString("name"));
            }
            return Optional.of(user);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }
}
