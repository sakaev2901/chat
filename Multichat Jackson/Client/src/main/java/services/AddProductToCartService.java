package services;

import clients.SocketClient;
import view.AddingToCartView;

public class AddProductToCartService {
    SocketClient client;

    public AddProductToCartService(SocketClient client) {
        this.client = client;
    }

    public boolean addToCart() {
        AddingToCartView addingToCartView = new AddingToCartView();
        String jsonProduct = addingToCartView.openAddToCart();
        if(jsonProduct == null) {
            return false;
        } else  {
            client.sendMessage(jsonProduct);
            return true;
        }
    }
}
