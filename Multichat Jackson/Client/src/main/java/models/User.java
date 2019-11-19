package models;

import java.util.LinkedList;

public class User {
    private Integer id;
    private String name;
    private AuthData authData;
    private LinkedList<Integer> shoppingCart;
    private LinkedList<Integer> orders;

    public LinkedList<Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(LinkedList<Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public LinkedList<Integer> getOrders() {
        return orders;
    }

    public void setOrders(LinkedList<Integer> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
