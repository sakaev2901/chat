package services;

import models.User;
import servers.ChatMultiServer;

import java.util.LinkedHashMap;
import java.util.List;

public interface MessageService {
    void sendMessage(LinkedHashMap<String, String> message, List<ChatMultiServer.ClientHandler> clients, User user);

}
