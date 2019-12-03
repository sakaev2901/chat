package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Cart;
import repositories.*;
import models.Payload;
import models.Product;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;

public class CartServiceImpl implements CartService{
    Socket client;

    public CartServiceImpl(Socket client) {
        this.client = client;
    }

    public CartServiceImpl() {

    }

    public void addToCart(String jsonProduct, Integer userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Payload payload = objectMapper.readValue(jsonProduct, Payload.class);
            LinkedHashMap<String, String> payloadMap = (LinkedHashMap<String, String>)payload.getPayload();
            Integer productId = Integer.parseInt(payloadMap.get("id"));
            CartRepository cartRepository = new CartRepositoryImpl();
            cartRepository.save(userId, productId);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getCart(String jsonRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Payload payload = objectMapper.readValue(jsonRequest, Payload.class);
            LinkedHashMap<String, String> payloadMap = (LinkedHashMap<String, String>)payload.getPayload();
            Integer userId = Integer.parseInt(payloadMap.get("id"));
            CartRepository cartRepository = new CartRepositoryImpl();
            Cart cart = cartRepository.findById(userId).orElseThrow(() -> new IllegalStateException());
            ProductRepository productRepository = new ProductRepositoryImpl();
            for (Product product:
                 cart.getProducts()) {
                Product newProduct = productRepository.findById(product.getId())
                        .orElseThrow(() -> new IllegalStateException());
                product.setName(newProduct.getName());
                product.setPrice(newProduct.getPrice());
            }
            Payload<LinkedHashMap<String, Object>> payloadProducts = new Payload<>();
            payload.setHeader("Command");
            LinkedHashMap<String, Object> mapPayload = new LinkedHashMap<>();
            mapPayload.put("command", "get cart");
            mapPayload.put("cart", cart);
            payload.setPayload(mapPayload);
            try {
                String jacksonOfProducts = objectMapper.writeValueAsString(payload);
                return jacksonOfProducts;
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void buyCart(Integer id) {
        OrderRepository orderRepository = new OrderRepositoryImpl();
        CartRepository cartRepository = new CartRepositoryImpl();
        orderRepository.saveUsersOrder(id, cartRepository.findById(id).orElseThrow(() -> new IllegalStateException()));
        clearCart(id);
    }

    public void clearCart(Integer id) {
        CartRepositoryImpl cartDao = new CartRepositoryImpl();
        cartDao.deleteByUserId(id);
    }
}