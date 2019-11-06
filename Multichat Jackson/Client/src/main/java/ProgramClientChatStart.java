import clients.SocketClient;
import controllers.MenuController;

import java.util.Scanner;

public class ProgramClientChatStart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SocketClient client = new SocketClient();
        client.startConnection("localhost", 6666);
        MenuController menuController = new MenuController(client);
        menuController.openWelcomeMenu();
        client.setMenuController(menuController);
//        while (true) {
////            String message = scanner.nextLine();
////            client.sendMessage( message);
//
//        }

    }
}
