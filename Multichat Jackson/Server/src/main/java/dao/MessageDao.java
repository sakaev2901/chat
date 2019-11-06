package dao;

import models.Message;

import java.util.LinkedList;

public interface MessageDao extends BaseDao<Message> {

    LinkedList<Message> findAllWithPagination(Integer size, Integer page);
}
