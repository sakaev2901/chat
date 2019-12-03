package repositories;

import models.Cart;

public interface CartRepository extends CrudRepository<Cart, Integer>{
    void save(Integer userId, Integer productId);
    public void deleteByUserId(Integer id);
}
