package models;

import java.util.LinkedList;

public class ShopList {
    private LinkedList<Product> products;

    public LinkedList<Product> getProducts() {
        return products;
    }

    public void setProducts(LinkedList<Product> products) {
        this.products = products;
    }
}
