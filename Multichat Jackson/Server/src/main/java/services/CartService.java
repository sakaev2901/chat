package services;

import models.Cart;
import protocol.Request;

public interface CartService {
    void addToCart(Request request, Integer userId);
    Cart getCart(Request request);
    void buyCart(Integer id);
    void clearCart(Integer id);
}
