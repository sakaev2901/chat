package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.Component;
import models.Message;
import models.Pagination;
import models.Payload;
import protocol.Request;
import repositories.MessageRepository;


import java.util.LinkedList;
import java.util.List;

public class PaginationServiceImpl implements PaginationService, Component {
    private MessageRepository messageRepository;

    public Pagination getMessages(Request request) {
        Integer size = Integer.parseInt(request.getParameter("size"));
        Integer page = Integer.parseInt(request.getParameter("page"));
        List<Message> messages = messageRepository.findAllWithPagination(size, page);
        Pagination pagination = new Pagination();
        pagination.setMessages(messages);
        return pagination;

    }



    @Override
    public String getComponentName() {
        return "paginationServiceImpl";
    }
}
