package dao;

import config.ConnectionConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CartDaoImpl {
    private final ConnectionConfig CONFIG = ConnectionConfig.getInstance();


    private final String SAVE = "INSERT INTO user_products_cart values (?, ?);";
    private final String DELETE_BY_USER_ID = "DELETE FROM user_products_cart WHERE \"user_id\" = ?;";
    private final String FIND_ALL = "SELECT * FROM user_products_cart WHERE \"user_id\" = ?;";

    public void save(Integer userId, Integer productId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(SAVE);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

    public void deleteByUserId(Integer id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(DELETE_BY_USER_ID);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

    public LinkedList<Integer> findAll(Integer user_id) {
        LinkedList<Integer> products = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            products = new LinkedList<>();
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_ALL);
            statement.setInt(1, user_id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                products.add(set.getInt(2));
            }
            return products;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }
}
