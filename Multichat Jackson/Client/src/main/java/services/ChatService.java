package services;

import clients.SocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import models.Payload;
import view.MenuView;

import java.util.Scanner;

public class ChatService {
    SocketClient client;

    public ChatService(SocketClient client) {
        this.client = client;
    }

    public void doChat() {
        new Thread(chatTasker).start();
    }

    public Runnable chatTasker = new Runnable() {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String messageText = scanner.nextLine();
                if (messageText.equals("$menu")) {
                    new MenuService(client).receiveMenu();
                    break;
                }
                Message message = new Message(messageText);
                Payload<Message> payload = new Payload("Message", message);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    String jsonValue = mapper.writeValueAsString(payload);
                    client.sendMessage(jsonValue);
//                    System.out.println(client.getIn().readLine());
                } catch (Exception e) {
                    throw new IllegalStateException(e);

                }
            }
        }
    };
}
