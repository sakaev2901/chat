package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import models.Payload;
import repositories.MessageRepository;
import repositories.MessageRepositoryImpl;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PaginationServiceImpl implements PaginationService{
    public String getMessages(Integer page, Integer size) {
        MessageRepository messageRepository = new MessageRepositoryImpl();
        Payload<LinkedList<Message>> payloadOfMessages = new Payload<>();
        LinkedHashMap<String, LinkedList<Message>> data = new LinkedHashMap<>();
        LinkedList<Message> messages = messageRepository.findAllWithPagination(size, page);
        payloadOfMessages.setHeader("messageArchive");
        payloadOfMessages.setPayload(messages);
        data.put("data", messages);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonValue = mapper.writeValueAsString(payloadOfMessages);
            return jsonValue;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

    }
}
