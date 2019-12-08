package services;

import clients.SocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import view.MessageArchiveView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class MessageArchiveService {
    SocketClient client;


    public MessageArchiveService(SocketClient client) {
        this.client = client;
    }

    public void receiveArchive(ArrayList<LinkedHashMap<String, String>> messages)  {
        for (LinkedHashMap<String, String> m:
                messages) {
            System.out.println(m.get("timeStamp")+" <" + m.get("senderId")+">: "+m.get("text"));
        }
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        new MenuService(client).receiveMenu();

    }

    public void sendPagination() {
        Payload payload = new Payload();
        payload.setHeader("Command");
        payload.setPayload(new MessageArchiveView().openArhive());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = null;
        try {
            jsonValue = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        client.sendMessage(jsonValue);

    }
}
