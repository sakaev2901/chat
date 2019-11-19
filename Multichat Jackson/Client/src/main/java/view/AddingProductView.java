package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;
import models.Product;

import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class AddingProductView {
    public String openAddingProductService() {
        System.out.println("=== Add Product ===");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        Integer price = scanner.nextInt();
        ObjectMapper objectMapper = new ObjectMapper();
        Payload payload = new Payload();
        payload.setHeader("Command");
        LinkedHashMap<String, String> commandPayload = new LinkedHashMap<>();
        commandPayload.put("command", "set product");
        commandPayload.put("name", name);
        commandPayload.put("price", String.valueOf(price));
        payload.setPayload(commandPayload);
        try {
            String jsonProduct = objectMapper.writeValueAsString(payload);
            return jsonProduct;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
