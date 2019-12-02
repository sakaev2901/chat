package services;

import clients.SocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;
import net.Session;
import view.CartView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CartService {
    SocketClient client;

    public CartService(SocketClient client) {
        this.client = client;
    }

    public void getCart() {
        Session session = Session.getInstance();
        ObjectMapper objectMapper = new ObjectMapper();
        Payload payload = new Payload();
        payload.setHeader("Command");
        LinkedHashMap<String, String> commandPayload = new LinkedHashMap<>();
        commandPayload.put("command", "get cart");
        commandPayload.put("id", String.valueOf(session.getId()));
        payload.setPayload(commandPayload);
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            client.sendMessage(jsonPayload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void setCart(String jsonPayload) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(Payload.class, LinkedHashMap.class);
        try {
            Payload<LinkedHashMap<String, Object>> payload = objectMapper.readValue(jsonPayload, type);
            LinkedHashMap<String, LinkedHashMap<String, Object>> cart = (LinkedHashMap<String, LinkedHashMap<String, Object>>)payload.getPayload().get("cart");
            Object o = cart.get("products");
            ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> products = (ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>>)o;
            CartView cartView = new CartView();
            String responce = cartView.openCart(products);
            MenuService menuService = new MenuService(client);
            if (responce != null) {
                client.sendMessage(responce);
            }
            menuService.receiveMenu();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
