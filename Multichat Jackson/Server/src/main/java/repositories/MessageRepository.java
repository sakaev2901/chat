package repositories;

import models.Message;

import java.util.LinkedList;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    LinkedList<Message> findAllWithPagination(Integer size, Integer page);
}
