//package repositories;
//
//import config.ConnectionConfig;
//import context.Component;
//import models.Cart;
//import models.Order;
//import models.Product;
//
//import java.sql.*;
//import java.util.*;
//
//public class OrderRepositoryImpl implements OrderRepository, Component {
//    private final ConnectionConfig CONFIG =ConnectionConfig.getInstance();
//    private final String FIND_ALL = "SELECT * FROM public.order WHERE \"user_id\" = ?;";
//    private final String SAVE = "INSERT INTO public.order (user_id) VALUES (?);";
//    private final String M2M_BIND = "INSERT INTO order_product VALUES (?, ?)";
//    private final String FIND_PRODUCTS_ID = "SELECT * FROM order_product WHERE \"order_id\" = ?;";
//
//    public void saveUsersOrder(Integer userId, Cart cart) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        try {
//            connection = CONFIG.getConnection();
//            statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
//            statement.setInt(1, userId);
//            statement.executeUpdate();
//            ResultSet resultSet = statement.getGeneratedKeys();
//            Integer orderId = null;
//            if (resultSet.next()) {
//                orderId = resultSet.getInt(1);
//            }
//            statement = connection.prepareStatement(M2M_BIND);
//            for (Product product:
//                 cart.getProducts()) {
//                statement.setInt(1, orderId);
//                statement.setInt(2, product.getId());
//                statement.executeUpdate();
//            }
//        } catch (SQLException e) {
//            throw new IllegalStateException(e);
//        } finally {
//            CONFIG.close(statement);
//            CONFIG.close(connection);
//        }
//    }
//
//    public List<Order> findAllByUserId(Integer userId) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//
//        List<Order> orders = new ArrayList<>();
//        try {
//            connection = CONFIG.getConnection();
//            statement = connection.prepareStatement(FIND_ALL);
//            statement.setInt(1, userId);
//            ResultSet set = statement.executeQuery();
//            while (set.next()) {
//                Order order = new Order();
//                //TODO: CURRENT POSITION
//                order.setId(set.getInt(1));
//                order.setDate(set.getString(3));
//                orders.add(order);
//            }
//            statement = connection.prepareStatement(FIND_PRODUCTS_ID);
//            List<Product> products = null;
//            for (Order order:
//                 orders) {
//                Integer orderId = order.getId();
//                statement.setInt(1, orderId);
//                set = statement.executeQuery();
//                products = new ArrayList<>();
//                while (set.next()) {
//                    Product product = new Product();
//                    product.setId(set.getInt(2));
//                    products.add(product);
//                }
//                order.setProducts(products);
//            }
//            return orders;
//        } catch (SQLException e) {
//            throw new IllegalStateException(e);
//        } finally {
//            CONFIG.close(statement);
//            CONFIG.close(connection);
//        }
//    }
//
//    @Override
//    public void save(Order model) {
//
//    }
//
//    @Override
//    public List<Order> findAll() {
//        return null;
//    }
//
//    @Override
//    public Optional<Order> findById(Integer integer) {
//        return Optional.empty();
//    }
//
//    @Override
//    public String getComponentName() {
//        return "orderRepositoryImpl";
//    }
//}
