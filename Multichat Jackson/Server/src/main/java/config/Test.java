package config;

import models.Message;
import models.Product;
import models.User;
import repositories.jdbcTemplate.MessageRepositoryJdbcTemplateImpl;
import repositories.jdbcTemplate.OrderRepositoryJdbcTemplate;
import repositories.jdbcTemplate.ProductRepositoryJdbcTemplateImpl;
import repositories.jdbcTemplate.UsersRepositoryJdbcTemplateImpl;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        UsersRepositoryJdbcTemplateImpl usersRepositoryJdbcTemplate = new UsersRepositoryJdbcTemplateImpl();
        User user = new UsersRepositoryJdbcTemplateImpl().findById(3).orElse(new User());
        System.out.println(user.getName());
        System.out.println(usersRepositoryJdbcTemplate.findByMailAndPassword("d", "elvin2901").get().getName());
        usersRepositoryJdbcTemplate.updateVerifier("fff", 4);
        System.out.println(usersRepositoryJdbcTemplate.findVerifierById(4));

        MessageRepositoryJdbcTemplateImpl messageRepositoryJdbcTemplate = new MessageRepositoryJdbcTemplateImpl();
        List<Message> list = messageRepositoryJdbcTemplate.findAllWithPagination(5, 1);
        System.out.println(list.get(1));

        ProductRepositoryJdbcTemplateImpl productRepositoryJdbcTemplate = new ProductRepositoryJdbcTemplateImpl();
        productRepositoryJdbcTemplate.save(new Product("name", 444));
        System.out.println(productRepositoryJdbcTemplate.findAll().get(1));
        System.out.println(productRepositoryJdbcTemplate.findById(2));

        OrderRepositoryJdbcTemplate orderRepositoryJdbcTemplate = new OrderRepositoryJdbcTemplate();
        System.out.println(orderRepositoryJdbcTemplate.findAllByUserId(3));
    }
}
