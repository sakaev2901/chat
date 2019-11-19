import clients.SocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.MessageResolver;
import models.Message;
import models.Payload;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        Message message = new Message("Ainur pimodor");
        Payload<Message> payload = new Payload<>();
        payload.setHeader("Message");
        payload.setPayload(message);

        LinkedList<Message> linkedList = new LinkedList<>();
        linkedList.add(message);
        linkedList.add(message);
        linkedList.add(message);
        Payload<LinkedList<Message>> payload1 = new Payload<>();
        payload1.setHeader("messageArchive");
        payload1.setPayload(linkedList);
        MessageResolver messageResolver = new MessageResolver();
//        try {
            messageResolver.resolve(new ObjectMapper().writeValueAsString(payload1));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        Payload<Boolean> status = new Payload<>();
//        status.setHeader("Login");
//        status.setPayload(true);
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println(objectMapper.writeValueAsString(status));
//        System.out.println(objectMapper.writeValueAsString(payload));
//        MessageResolver messageResolver = new MessageResolver();
//        messageResolver.resolve(objectMapper.writeValueAsString(status));
    }
}
