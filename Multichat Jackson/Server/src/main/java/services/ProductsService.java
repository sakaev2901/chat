package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import repositories.ProductRepositoryImpl;
import models.Payload;
import models.Product;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ProductsService {
    public String getProducts() {
        LinkedList<Product> products = new ProductRepositoryImpl().findAll();
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

    public void addProduct(String jsonProduct) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Payload payload = objectMapper.readValue(jsonProduct, Payload.class);
            LinkedHashMap<String, String> payloadMap = (LinkedHashMap<String, String>)payload.getPayload();
            Product product = new Product();
            product.setName(payloadMap.get("name"));
            product.setPrice(Integer.parseInt(payloadMap.get("price")));
            ProductRepositoryImpl productDao = new ProductRepositoryImpl();
            productDao.save(product);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
