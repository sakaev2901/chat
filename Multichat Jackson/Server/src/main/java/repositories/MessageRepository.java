package repositories;

import models.Message;

import java.util.LinkedList;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

   List<Message> findAllWithPagination(Integer size, Integer page);
}
