package view;

import models.Payload;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class MessageArchiveView {
    public LinkedHashMap<String, String> openArhive() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Message Archive ===");
        System.out.print("Size: ");
        Integer size = scanner.nextInt();
        System.out.print("Page: ");
        Integer page = scanner.nextInt();
        Payload payload = new Payload();
        payload.setHeader("Command");
        LinkedHashMap<String, String> commandPayload = new LinkedHashMap<>();
        commandPayload.put("command", "get messages");
        commandPayload.put("page", String.valueOf(page));
        commandPayload.put("size", String.valueOf(size));
        payload.setPayload(commandPayload);
        return commandPayload;
    }
}
