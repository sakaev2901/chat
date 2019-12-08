package services;

import models.Cart;
import protocol.Request;

public interface CartService {
    void addToCart(Request request);
    Cart getCart(Request request);
    void buyCart(Request request);
    void clearCart(Request request);
}
