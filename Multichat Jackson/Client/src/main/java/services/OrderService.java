package services;

import clients.SocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;
import net.Session;
import view.OrdersView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class OrderService {
    private SocketClient client;

    public OrderService(SocketClient client) {
        this.client = client;
    }

    public void getOrders() {
        Payload<LinkedHashMap<String, String>> payload = new Payload<>();
        payload.setHeader("Command");
        LinkedHashMap<String, String> commandPayload = new LinkedHashMap<>();
        commandPayload.put("command", "get orders");
        commandPayload.put("id", String.valueOf(Session.getInstance().getId()));
        payload.setPayload(commandPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jacksonPayload = objectMapper.writeValueAsString(payload);
            client.sendMessage(jacksonPayload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setOrders(String request) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(Payload.class, LinkedHashMap.class);
        try {
            Payload<LinkedHashMap<String, Object>> payload = objectMapper.readValue(request, type);
            System.out.println();
            ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>)payload.getPayload().get("orders");
            OrdersView ordersView = new OrdersView();
            ordersView.openOrders(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
