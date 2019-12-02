package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Cart;
import repositories.CartDaoImpl;
import repositories.OrderRepositoryImpl;
import repositories.ProductRepositoryImpl;
import models.Payload;
import models.Product;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CartService {
    Socket client;

    public CartService(Socket client) {
        this.client = client;
    }

    public CartService() {

    }

    public void addToCart(String jsonProduct, Integer userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Payload payload = objectMapper.readValue(jsonProduct, Payload.class);
            LinkedHashMap<String, String> payloadMap = (LinkedHashMap<String, String>)payload.getPayload();
            Integer productId = Integer.parseInt(payloadMap.get("id"));
            CartDaoImpl cartDao = new CartDaoImpl();
            cartDao.save(userId, productId);
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
            CartDaoImpl cartDao = new CartDaoImpl();
            Cart cart = cartDao.findById(userId);
            ProductRepositoryImpl productDao = new ProductRepositoryImpl();
            for (Product product:
                 cart.getProducts()) {
                Product newProduct = productDao.findById(product.getId())
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
        OrderRepositoryImpl orderDao = new OrderRepositoryImpl();
        CartDaoImpl cartDao = new CartDaoImpl();
        orderDao.saveUsersOrder(id, cartDao.findById(id));
        clearCart(id);
    }

    public void clearCart(Integer id) {
        CartDaoImpl cartDao = new CartDaoImpl();
        cartDao.deleteByUserId(id);
    }
}