package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;
import net.Session;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class CartView {
    public String openCart(ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> products) {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = null;
        Boolean flag = false;
        Scanner scanner = new Scanner(System.in);
        Payload payload = new Payload();
        payload.setHeader("Command");
        LinkedHashMap<String, String> commandPayload = new LinkedHashMap<>();
        Session session = Session.getInstance();
        System.out.println("=== Cart ===");
        for (LinkedHashMap<String, LinkedHashMap<String, String>> item:
                products) {
            System.out.println(item.get("name") + " " + item.get("price"));
        }
        do {
            System.out.println("1. Buy");
            System.out.println("2. Clear");
            System.out.println("3. Menu");
            Integer button = scanner.nextInt();
            if (products.isEmpty()) {
                return null;
            }
            switch (button) {
                case 1: {
                    commandPayload.put("command", "buy cart");
                    commandPayload.put("id", String.valueOf(Session.getInstance().getId()));

                }
                break;
                case 2: {
                    commandPayload.put("command", "clear cart");
                    commandPayload.put("id", String.valueOf(Session.getInstance().getId()));


                }
                break;
                case 3: {
                    return null;
                }
                default: {
                    System.err.println("Wrong button");
                    flag = true;
                }
            }
        } while (flag);
        try {
            commandPayload.put("id", String.valueOf(session.getId()));
            payload.setPayload(commandPayload);
            response = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        return response;
    }
}
