package services;

import clients.SocketClient;
import net.Session;
import view.MenuView;

public class MenuService {
    SocketClient client;

    public MenuService(SocketClient client) {
        this.client = client;
    }

    public void receiveMenu() {
        Integer button = new MenuView().openMenu();
        if(button == 1) {
            new ChatService(client).doChat();
        } else if (button == 2) {
            new MessageArchiveService(client).sendPagination();
        } else if (button == 3 && Session.getInstance().getRole().equals("user")) {
            new ShopService(client).getProducts();
        }

    }
}
