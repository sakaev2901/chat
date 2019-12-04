package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.Component;
import models.Cart;
import protocol.Request;
import repositories.*;
import models.Payload;
import models.Product;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;

public class CartServiceImpl implements CartService, Component {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;


    public CartServiceImpl() {

    }

    public void addToCart(Request request, Integer userId) {
            Integer productId = Integer.parseInt(request.getParameter("id"));
            cartRepository.save(userId, productId);
    }

    public Cart getCart(Request request) {
            Integer userId = Integer.parseInt(request.getParameter("id"));
            Cart cart = cartRepository.findById(userId).orElseThrow(() -> new IllegalStateException());
            for (Product product:
                 cart.getProducts()) {
                Product newProduct = productRepository.findById(product.getId())
                        .orElseThrow(() -> new IllegalStateException());
                product.setName(newProduct.getName());
                product.setPrice(newProduct.getPrice());
            }
         return cart;
    }

    public void buyCart(Integer id) {
        orderRepository.saveUsersOrder(id, cartRepository.findById(id).orElseThrow(() -> new IllegalStateException()));
        clearCart(id);
    }

    public void clearCart(Integer id) {
        cartRepository.deleteByUserId(id);
    }

    @Override
    public String getComponentName() {
        return null;
    }

    public CartRepository getCartRepository() {
        return cartRepository;
    }

    public void setCartRepository(CartRepositoryImpl cartRepository) {
        this.cartRepository = cartRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepositoryImpl orderRepository) {
        this.orderRepository = orderRepository;
    }
}