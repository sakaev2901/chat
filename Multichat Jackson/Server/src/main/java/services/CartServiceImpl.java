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

    public void addToCart(Request request) {
        Integer userId= Integer.parseInt(request.getParameter("user_id"));

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

    public void buyCart(Request request) {
        Integer id = Integer.parseInt(request.getParameter("id"));

        orderRepository.saveUsersOrder(id, cartRepository.findById(id).orElseThrow(() -> new IllegalStateException()));
        clearCart(request);
    }

    public void clearCart(Request request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        cartRepository.deleteByUserId(id);
    }

    @Override
    public String getComponentName() {
        return "cartServiceImpl";
    }

    public CartRepository getCartRepository() {
        return cartRepository;
    }



    public ProductRepository getProductRepository() {
        return productRepository;
    }



    public OrderRepository getOrderRepository() {
        return orderRepository;
    }


}