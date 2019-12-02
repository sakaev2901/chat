package repositories;

import models.Cart;
import models.Order;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    void saveUsersOrder(Integer userId, Cart cart);
    List<Order> findAllByUserId(Integer userId);

}
