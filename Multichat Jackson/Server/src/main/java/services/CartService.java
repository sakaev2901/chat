package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CartDaoImpl;
import dao.OrderDaoImpl;
import dao.ProductDaoImpl;
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
            LinkedList<Integer> prodictsId = cartDao.findAll(userId);
            LinkedList<Product> cart = new LinkedList<>();
            ProductDaoImpl productDao = new ProductDaoImpl();
            for (Integer id:
                 prodictsId) {
                cart.add(productDao.findById(id));
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
        OrderDaoImpl orderDao = new OrderDaoImpl();
        CartDaoImpl cartDao = new CartDaoImpl();
        orderDao.save(id, cartDao.findAll(id));
        clearCart(id);
    }

    public void clearCart(Integer id) {
        CartDaoImpl cartDao = new CartDaoImpl();
        cartDao.deleteByUserId(id);
    }
}