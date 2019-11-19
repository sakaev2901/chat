package dao;

import config.ConnectionConfig;
import models.User;

import java.sql.*;
import java.util.LinkedList;

public class OrderDaoImpl {
    private final ConnectionConfig CONFIG =ConnectionConfig.getInstance();
    private final String FIND_ALL = "SELECT * FROM products";
    private final String SAVE = "INSERT INTO public.order (user_id) VALUES (?);";
    private final String M2M_BIND = "INSERT INTO order_product VALUES (?, ?)";

    public void save(Integer userId, LinkedList<Integer> products) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            Integer orderId = null;
            if (resultSet.next()) {
                orderId = resultSet.getInt(1);
            }
            statement = connection.prepareStatement(M2M_BIND);
            for (Integer productId:
                 products) {
                statement.setInt(1, orderId);
                statement.setInt(2, productId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

}
