package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MessageDaoImpl;
import models.Message;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PaginationService {
    public String getMessages(Integer page, Integer size) {
        MessageDaoImpl messageDao = new MessageDaoImpl();
        LinkedHashMap<String, LinkedList<Message>> data = new LinkedHashMap<>();
        LinkedList<Message> messages = messageDao.findAllWithPagination(size, page);
        data.put("data", messages);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonValue = mapper.writeValueAsString(data);
            return jsonValue;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

    }
}
