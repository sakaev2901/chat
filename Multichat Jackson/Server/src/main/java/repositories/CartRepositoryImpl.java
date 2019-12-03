package repositories;

import config.ConnectionConfig;
import models.Cart;
import models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartRepositoryImpl implements CartRepository{
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

    @Override
    public void save(Cart model) {

    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    public Optional<Cart> findById(Integer userId) {
        Cart cart = new Cart();
        List<Product> products = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            products = new ArrayList<>();
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_ALL);
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Product product = new Product();
                product.setId(set.getInt(2));
                products.add(product);
            }
            cart.setProducts(products);
            return Optional.of(cart);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }
}
