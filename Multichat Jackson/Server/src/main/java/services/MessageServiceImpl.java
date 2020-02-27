package services;

import context.Component;
import models.Message;
import protocol.Request;
import repositories.MessageRepository;

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
        return "messageServiceImpl";
    }


}
