package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class AddingToCartView {
    public String openAddToCart() {
        String jsonProduct = null;
        String answer = "";
        System.out.print("Do you want to buy smthing?(y/n)");
        Scanner scanner = new Scanner(System.in);
        while (!answer.equals("y") && !answer.equals("n")) {
            answer = scanner.nextLine();
            if (!answer.equals("y") && !answer.equals("n")) {
                System.err.println("Wrong button!");
            }
        }
        if (answer.equals("n")) {
            return jsonProduct;
        } else  {

            System.out.print("Select product's number");
            Integer id = scanner.nextInt();
            ObjectMapper objectMapper = new ObjectMapper();
            Payload payload = new Payload();
            payload.setHeader("Command");
            LinkedHashMap<String, String> commandPayload = new LinkedHashMap<>();
            commandPayload.put("command", "set product to cart");
            commandPayload.put("id", String.valueOf(id));
            payload.setPayload(commandPayload);
            try {
                jsonProduct = objectMapper.writeValueAsString(payload);
                return jsonProduct;
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
