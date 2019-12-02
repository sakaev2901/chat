package repositories;

import models.Product;

import java.util.LinkedList;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    LinkedList<Product> findAll();
}
