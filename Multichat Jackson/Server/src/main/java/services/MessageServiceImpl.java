package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import models.Payload;
import models.User;
import protocol.Request;
import repositories.MessageRepository;
import repositories.MessageRepositoryImpl;
import servers.ChatMultiServer;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

public class MessageServiceImpl implements MessageService{

    @Override
    public Message sendMessage(Request request) {
        try {
            Message userMessage = new Message();
            userMessage.setSenderName(request.getParameter("senderId"));
            userMessage.setText(request.getParameter("text"));
            userMessage.setTimeStamp(request.getParameter("timeStamp"));
//            MessageRepository messageRepository = new MessageRepositoryImpl();
//            messageRepository.save(userMessage);
            return userMessage;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
            }
}
