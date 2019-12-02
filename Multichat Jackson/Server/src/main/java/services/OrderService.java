package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Order;
import repositories.OrderRepositoryImpl;
import repositories.ProductRepositoryImpl;
import models.Payload;
import models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderService {


    public String getOrders(Integer userId) {
        OrderRepositoryImpl orderDao = new OrderRepositoryImpl();
        ProductRepositoryImpl productDao = new ProductRepositoryImpl();
        List<Product> products = null;
        List<Order> orders = orderDao.findAllByUserId(userId);
        for (Order order:
             orders) {
            products = order.getProducts();
            for (Product product:
                 products) {
                Product newProduct = productDao
                        .findById(product.getId())
                        .orElseThrow(() -> new IllegalStateException(String.valueOf(product.getId())));
                product.setPrice(newProduct.getPrice());
                product.setName(newProduct.getName());
            }
        }
        Payload<LinkedHashMap<String, Object>> payload = new Payload<>();
        payload.setHeader("Command");
        LinkedHashMap<String, Object> mapPayload = new LinkedHashMap<>();
        mapPayload.put("command", "get orders");
        mapPayload.put("data", orders);
        payload.setPayload(mapPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String request = objectMapper.writeValueAsString(payload);
            return request;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
