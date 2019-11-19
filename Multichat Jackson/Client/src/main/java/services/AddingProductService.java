package services;

import clients.SocketClient;
import view.AddingProductView;

public class AddingProductService {
    SocketClient client;

    public AddingProductService(SocketClient client) {
        this.client = client;
    }

    public void addProduct() {
        AddingProductView addingProductView = new AddingProductView();
        String jsonProduct = addingProductView.openAddingProductService();
        client.sendMessage(jsonProduct);
    }
}
