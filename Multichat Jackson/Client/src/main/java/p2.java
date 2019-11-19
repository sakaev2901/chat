import clients.SocketClient;
import controllers.MenuController;

import java.util.Scanner;

public class p2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SocketClient client = new SocketClient();
        client.startConnection("localhost", 6666);


    }
}
