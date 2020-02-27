package repositories.jdbcTemplate;

import config.JdbcTemplateConfig;
import context.Component;
import models.Cart;
import models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import repositories.CartRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class CartRepositoryJdbcTemplateImpl implements CartRepository, Component {

    private final String SAVE = "INSERT INTO user_products_cart values (?, ?);";
    private final String DELETE_BY_USER_ID = "DELETE FROM user_products_cart WHERE \"user_id\" = ?;";
    private final String FIND_ALL = "SELECT * FROM user_products_cart WHERE \"user_id\" = ?;";

    private JdbcTemplate jdbcTemplate = JdbcTemplateConfig.getInstance().getJdbcTemplate();
    private RowMapper<Product> productRowMapper = (row, rowNum) ->
            Product.builder()
            .id(row.getInt(2))
            .build();

    @Override
    public void save(Integer userId, Integer productId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SAVE);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            return statement;
        });
    }

    @Override
    public void deleteByUserId(Integer id) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_USER_ID);
            statement.setInt(1, id);
            return statement;
        });
    }

    @Override
    public void save(Cart model) {

    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public Optional<Cart> findById(Integer id) {
        Cart cart = new Cart();
        cart.setProducts(jdbcTemplate.query(FIND_ALL, new Object[]{id}, productRowMapper));
        return Optional.ofNullable(cart);
    }

    @Override
    public String getComponentName() {
        return "CartRepositoryJdbcTemplateImpl";
    }

}
