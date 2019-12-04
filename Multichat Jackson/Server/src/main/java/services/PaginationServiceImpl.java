package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.Component;
import models.Message;
import models.Pagination;
import models.Payload;
import protocol.Request;
import repositories.MessageRepository;
import repositories.MessageRepositoryImpl;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PaginationServiceImpl implements PaginationService, Component {
    public Pagination getMessages(Request request) {
        Integer size = Integer.parseInt(request.getParameter("size"));
        Integer page = Integer.parseInt(request.getParameter("page"));
        MessageRepository messageRepository = new MessageRepositoryImpl();
        Payload<LinkedList<Message>> payloadOfMessages = new Payload<>();
        LinkedHashMap<String, LinkedList<Message>> data = new LinkedHashMap<>();
        LinkedList<Message> messages = messageRepository.findAllWithPagination(size, page);
//        payloadOfMessages.setHeader("messageArchive");
//        payloadOfMessages.setPayload(messages);
//        data.put("data", messages);
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            String jsonValue = mapper.writeValueAsString(payloadOfMessages);
//            return jsonValue;
//        } catch (JsonProcessingException e) {
//            throw new IllegalStateException(e);
//        }
        Pagination pagination = new Pagination();
        pagination.setMessages(messages);
        return pagination;

    }

    @Override
    public String getComponentName() {
        return null;
    }
}
