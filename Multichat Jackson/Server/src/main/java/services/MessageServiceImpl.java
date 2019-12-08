package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import context.Component;
import models.Message;
import models.Payload;
import models.User;
import protocol.Request;
import repositories.CartRepository;
import repositories.MessageRepository;
import repositories.MessageRepositoryImpl;
import servers.ChatMultiServer;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

public class MessageServiceImpl implements MessageService, Component {

    private MessageRepository messageRepository;

    @Override
    public Message sendMessage(Request request) {
        try {
            Message userMessage = new Message();
            userMessage.setSenderName(request.getParameter("senderId"));
            userMessage.setText(request.getParameter("text"));
            userMessage.setTimeStamp(request.getParameter("timeStamp"));
            messageRepository.save(userMessage);
            return userMessage;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
            }

    @Override
    public String getComponentName() {
        return null;
    }

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public void setMessageRepository(MessageRepositoryImpl messageRepository) {
        this.messageRepository = messageRepository;
    }
}
