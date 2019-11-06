package services;

import dao.MessageDaoImpl;
import models.Message;
import models.Payload;
import models.User;
import servers.ChatMultiServer;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MessageService {

    public void sendMessage(LinkedHashMap<String, String> message, List<ChatMultiServer.ClientHandler> clients, User user) {
        try {
            Message userMessage = new Message();
            userMessage.setSenderName(user.getName());
            userMessage.setText(message.get("text"));
            userMessage.setTimeStamp(message.get("timeStamp"));
            MessageDaoImpl messageDao = new MessageDaoImpl();
            messageDao.save(userMessage);
            for (ChatMultiServer.ClientHandler client:
                    clients) {
                PrintWriter out = new PrintWriter(client.getClientSocket().getOutputStream(), true);
                out.println(userMessage.getTimeStamp() + " " + "<"+userMessage.getSenderName() + ">: " + userMessage.getText() );
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
            }
}
