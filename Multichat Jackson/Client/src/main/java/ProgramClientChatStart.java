import clients.SocketClient;
import controllers.MenuController;

import java.util.Scanner;

public class ProgramClientChatStart {
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        client.startConnection("localhost", 6666);


    }
}
