package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ProductDao;
import dao.ProductDaoImpl;
import models.Payload;
import models.Product;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ProductsService {
    public String getProducts() {
        LinkedList<Product> products = new ProductDaoImpl().findAll();
        Payload<LinkedHashMap<String, Object>> payload = new Payload<>();
        payload.setHeader("Command");
        LinkedHashMap<String, Object> mapPayload = new LinkedHashMap<>();
        mapPayload.put("command", "get products");
        mapPayload.put("data", products);
        payload.setPayload(mapPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jacksonOfProducts = objectMapper.writeValueAsString(payload);
            return jacksonOfProducts;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
