package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import models.Payload;
import models.User;
import repositories.MessageRepository;
import repositories.MessageRepositoryImpl;
import servers.ChatMultiServer;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

public class MessageServiceImpl implements MessageService{

    public void sendMessage(LinkedHashMap<String, String> message, List<ChatMultiServer.ClientHandler> clients, User user) {
        try {
            Message userMessage = new Message();
            userMessage.setSenderName(user.getName());
            userMessage.setText(message.get("text"));
            userMessage.setTimeStamp(message.get("timeStamp"));
            MessageRepository messageRepository = new MessageRepositoryImpl();
            messageRepository.save(userMessage);
            Payload<Message> messagePayload = new Payload<>();
            messagePayload.setHeader("Message");
            messagePayload.setPayload(userMessage);
            String jacksonMessage = new ObjectMapper().writeValueAsString(messagePayload);
            for (ChatMultiServer.ClientHandler client:
                    clients) {
                PrintWriter out = new PrintWriter(client.getClientSocket().getOutputStream(), true);
//                out.println(userMessage.getTimeStamp() + " " + "<"+userMessage.getSenderName() + ">: " + userMessage.getText() );
//                  out.println("{\"header\":\"Message\",\"payload\":{\"senderName\":\"Eldar\",\"text\":\"Ainur pimodor\",\"timeStamp\":\"2019.11.13_08:42:39\"}}");
                out.println(jacksonMessage);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
            }
}
