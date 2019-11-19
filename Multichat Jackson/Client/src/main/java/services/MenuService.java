package services;

import clients.SocketClient;
import net. Session;
import view.MenuView;

import java.util.Scanner;

public class MenuService {
    SocketClient client;

    public MenuService(SocketClient client) {
        this.client = client;
    }

    public void receiveMenu() {
        Integer button = new MenuView().openMenu();
        String role = Session.getInstance().getRole();
        if(button == 1) {
            new ChatService(client).doChat();
        } else if (button == 2) {
            new MessageArchiveService(client).sendPagination();
        } else if(role.equals("user")) {
            if (button == 3) {
                new ShopService(client).getProducts();
            }
        } else if(role.equals("admin")) {
            if (button == 3) {
                AddingProductService addingProductService = new AddingProductService(client);
                addingProductService.addProduct();
                System.out.print("Press Enter to go menu");
                new Scanner(System.in).hasNextLine();
                receiveMenu();
            }  else if (button == 4) {
                new ShopService(client).getProducts();
            }
        }

    }
}