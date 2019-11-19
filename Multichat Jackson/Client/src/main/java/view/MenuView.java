
package view;

import net.Session;

import java.util.Scanner;

public class MenuView {
    public Integer openMenu() {
            Scanner scanner = new Scanner(System.in);
            Session session = Session.getInstance("user");
            System.out.println("=== MENU ===");
            System.out.println("1. Go to the chat");
            System.out.println("2. Message archive");
            if (session.getRole().equals("admin")) {
                System.out.println("3. Add product");
            } else {
                System.out.println("3. Shop");
                System.out.println("4. Shopping cart");
            }
            System.out.println("0. Logout");
            Integer button = scanner.nextInt();
            return button;
//            if (button == 1) {
//                System.out.println("=== CHAT ===");
//                openChat();
//                break;
//            } else if(button == 2) {
//                openMessageArchive();
//                break;
//            }
//            else if (button == 3) {
//                logout();
//                openLoginPage();
//                break;
//            } else {
//                System.out.println("Wrong button");
//            }
    }
}
