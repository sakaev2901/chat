package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Or;
import context.Component;
import models.Order;
import models.OrderList;
import protocol.Request;
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

public class OrderServiceImpl implements OrderService, Component {


    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public OrderList getOrders(Integer userId) {
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
        OrderList orderList = new OrderList();
        orderList.setOrders(orders);
        return orderList;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepositoryImpl orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String getComponentName() {
        return null;
    }
}
