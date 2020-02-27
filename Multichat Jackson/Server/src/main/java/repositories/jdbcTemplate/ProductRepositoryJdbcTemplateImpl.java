package repositories.jdbcTemplate;

import config.JdbcTemplateConfig;
import context.Component;
import models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import repositories.ProductRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryJdbcTemplateImpl implements ProductRepository, Component {

    private final String FIND_ALL = "SELECT * FROM products";
    private final String SAVE = "INSERT INTO products (name, price) VALUES (?, ?);";
    private final String FIND_BY_ID = "SELECT * FROM  products WHERE \"id\" = ?;";

    private JdbcTemplate jdbcTemplate = JdbcTemplateConfig.getInstance().getJdbcTemplate();
    private RowMapper<Product> productRowMapper = (row, rowNum) ->
            Product.builder()
            .id(row.getInt("id"))
            .name(row.getString("name"))
            .price(row.getInt("price"))
            .build();




    @Override
    public void save(Product model) {
        jdbcTemplate.update(SAVE, model.getName(), model.getPrice());
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(FIND_ALL, productRowMapper);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, productRowMapper));
    }

    @Override
    public String getComponentName() {
        return "ProductRepositoryJdbcTemplateImpl";
    }
}
