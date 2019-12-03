package services;

public interface CartService {
    void addToCart(String json, Integer userId);
    String getCart(String jsonRequest);
    void buyCart(Integer id);
    void clearCart(Integer id);
}
