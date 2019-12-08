package services;

import models.ShopList;
import protocol.Request;

public interface ProductService {
    ShopList getProducts();
    void addProduct(Request request);
}
