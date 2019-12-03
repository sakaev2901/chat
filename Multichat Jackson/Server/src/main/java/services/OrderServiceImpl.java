package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Order;
import repositories.OrderRepository;
import repositories.OrderRepositoryImpl;
import repositories.ProductRepository;
import repositories.ProductRepositoryImpl;
import models.Payload;
import models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderServiceImpl implements OrderService{


    public String getOrders(Integer userId) {
        OrderRepository orderRepository = new OrderRepositoryImpl();
        ProductRepository productRepository = new ProductRepositoryImpl();
        List<Product> products = null;
        List<Order> orders = orderRepository.findAllByUserId(userId);
        for (Order order:
             orders) {
            products = order.getProducts();
            for (Product product:
                 products) {
                Product newProduct = productRepository
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
