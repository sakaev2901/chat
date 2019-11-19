package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CartDaoImpl;
import dao.ProductDaoImpl;
import models.Payload;
import models.Product;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;

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
}