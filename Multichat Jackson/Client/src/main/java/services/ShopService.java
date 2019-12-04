package services;

import clients.SocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;
import view.ShopView;

import javax.crypto.spec.OAEPParameterSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ShopService {
    SocketClient client;
    ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> products = new ArrayList<>();
    public ShopService(SocketClient client) {
        this.client = client;
    }

    public void getProducts() {
        Payload<LinkedHashMap<String, String>> payload = new Payload<>();
        payload.setHeader("Command");
        LinkedHashMap<String, String> commandPayload = new LinkedHashMap<>();
        commandPayload.put("command", "get products");
        payload.setPayload(commandPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jacksonPayload = objectMapper.writeValueAsString(payload);
            client.sendMessage(jacksonPayload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

    }

    public void receiveProducts(String responce) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(Payload.class, LinkedHashMap.class);
        try {
            Payload<LinkedHashMap<String, Object>> payload = objectMapper.readValue(responce, type);
            this.products = (ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>>)payload.getPayload().get("products");
            receiveUsersOrder();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void receiveUsersOrder() {
        ShopView shopView = new ShopView();
        shopView.doShop(products);
    }
}
