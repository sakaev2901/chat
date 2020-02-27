package repositories;

import models.Product;

import java.util.LinkedList;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    List<Product> findAll();
}
