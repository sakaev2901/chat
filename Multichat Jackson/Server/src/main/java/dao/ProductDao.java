package dao;

import models.Product;

import java.util.LinkedList;

public interface ProductDao extends BaseDao<Product>{
    LinkedList<Product> findAll();
}
