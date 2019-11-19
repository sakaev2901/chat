package services;

import clients.SocketClient;
import view.LoginView;

import java.net.Socket;

public class LoginService {
    SocketClient client;

    public LoginService(SocketClient client) {
        this.client = client;
    }

    public void doLogin() {
        client.sendMessage(new LoginView().openLoginPage());
    }
}
