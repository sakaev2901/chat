package dao;

import config.ConnectionConfig;
import models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class ProductDaoImpl implements ProductDao {

    private final ConnectionConfig CONFIG =ConnectionConfig.getInstance();
    private final String FIND_ALL = "SELECT * FROM products";
    private final String SAVE = "INSERT INTO products (name, price) VALUES (?, ?);";

    @Override
    public LinkedList<Product> findAll() {
        LinkedList<Product> products = new LinkedList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_ALL);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                products.add(new Product(set.getInt("id"),
                        set.getString("name"), set.getInt("price")));
            }
            return products;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

    @Override
    public void save(Product model) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(SAVE);
            statement.setString(1, model.getName());
            statement.setInt(2, model.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(statement);
        }
    }
}
