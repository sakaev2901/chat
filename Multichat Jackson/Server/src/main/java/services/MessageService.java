package services;

import models.Message;
import models.User;
import protocol.Request;
import servers.ChatMultiServer;

import java.util.LinkedHashMap;
import java.util.List;

public interface MessageService {
    Message sendMessage(Request request);

}
