package repositories.jdbcTemplate;

import com.sun.rowset.internal.Row;
import config.JdbcTemplateConfig;
import context.Component;
import models.Cart;
import models.Order;
import models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import repositories.OrderRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryJdbcTemplate implements OrderRepository, Component {


    private final String FIND_ALL = "SELECT * FROM public.order WHERE \"user_id\" = ?;";
    private final String SAVE = "INSERT INTO public.order (user_id) VALUES (?);";
    private final String M2M_BIND = "INSERT INTO order_product VALUES (?, ?)";
    private final String FIND_PRODUCTS_ID = "SELECT * FROM order_product WHERE \"order_id\" = ?;";

    private JdbcTemplate jdbcTemplate = JdbcTemplateConfig.getInstance().getJdbcTemplate();
    private RowMapper<Order> orderRowMapper = (row, rowNum) ->
            Order.builder()
            .id(row.getInt("id"))
            .date(row.getString("time"))
            .build();

    private RowMapper<Product> productRowMapper = (row, rowNum) ->
            Product.builder()
            .id(row.getInt("product_id"))
            .build();

    @Override
    public void saveUsersOrder(Integer userId, Cart cart) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            return statement;
        }, keyHolder);
        Integer orderId = (Integer)keyHolder.getKey();
        for (Product product:
                cart.getProducts()) {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(M2M_BIND);
                statement.setInt(1, orderId);
                statement.setInt(2, product.getId());
                return statement;
            });
        }

    }

    @Override
    public List<Order> findAllByUserId(Integer userId) {
        List<Order> orders = jdbcTemplate.query(FIND_ALL, new Object[]{userId}, orderRowMapper);
        for (Order order:
             orders) {
            order.setProducts(jdbcTemplate.query(FIND_PRODUCTS_ID, new Object[]{order.getId()}, productRowMapper));
        }
        return orders;
    }

    @Override
    public void save(Order model) {

    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Optional<Order> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public String getComponentName() {
        return "OrderRepositoryJdbcTemplate";
    }
}
