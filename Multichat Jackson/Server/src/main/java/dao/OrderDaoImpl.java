package dao;

import config.ConnectionConfig;
import models.User;
import org.omg.CORBA.INTERNAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class OrderDaoImpl {
    private final ConnectionConfig CONFIG =ConnectionConfig.getInstance();
    private final String FIND_ALL = "SELECT * FROM public.order WHERE \"user_id\" = ?;";
    private final String SAVE = "INSERT INTO public.order (user_id) VALUES (?);";
    private final String M2M_BIND = "INSERT INTO order_product VALUES (?, ?)";
    private final String FIND_PRODUCTS_ID = "SELECT * FROM order_product WHERE \"order_id\" = ?;";

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

    public ArrayList<HashMap<String, Object>> findAll(Integer userId) {
        Connection connection = null;
        PreparedStatement statement = null;

        ArrayList<HashMap<String, Object>> orders = new ArrayList<>();
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_ALL);
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                HashMap<String, String> info = new HashMap<>();
                info.put("id", String.valueOf(set.getInt(1)));
                info.put("time", set.getString(3));
                HashMap<String, Object> data = new HashMap<>();
                data.put("info", info);
                orders.add(data);
            }
            statement = connection.prepareStatement(FIND_PRODUCTS_ID);
            ArrayList<Integer> productsId = null;
            for (HashMap<String, Object> data:
                 orders) {
                HashMap<String, String> info =  (HashMap<String, String>)data.get("info");
                statement.setInt(1, Integer.parseInt(info.get("id")));
                set = statement.executeQuery();
                productsId = new ArrayList<>();
                while (set.next()) {
                    productsId.add(set.getInt(2));
                }
                data.put("products", productsId);
            }
            return orders;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

}
