package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.OrderDaoImpl;
import dao.ProductDaoImpl;
import models.Payload;
import models.Product;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class OrderService {


    public String getOrders(Integer userId) {
        OrderDaoImpl orderDao = new OrderDaoImpl();
        ProductDaoImpl productDao = new ProductDaoImpl();
        ArrayList<Product> products = null;
        ArrayList<HashMap<String, Object>> orders = orderDao.findAll(userId);
        for (HashMap<String, Object> data:
             orders) {
            ArrayList<Integer> productsId = (ArrayList<Integer>)data.get("products");
            products = new ArrayList<>();
            for (Integer id:
                 productsId) {
                products.add(productDao.findById(id));
            }
            data.put("products", products);
        }
        Payload<LinkedHashMap<String, Object>> payload = new Payload<>();
        payload.setHeader("Command");
        LinkedHashMap<String, Object> mapPayload = new LinkedHashMap<>();
        mapPayload.put("command", "get orders");
        mapPayload.put("data", orders);
        payload.setPayload(mapPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String request = objectMapper.writeValueAsString(payload);
            return request;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
