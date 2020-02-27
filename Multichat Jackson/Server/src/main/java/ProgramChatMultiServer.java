import context.ApplicationContext;
import context.ApplicationContextReflectionBased;
import servers.ChatMultiServer;
import services.LoginService;

public class ProgramChatMultiServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContextReflectionBased();
        ChatMultiServer chatMultiServer = (ChatMultiServer) applicationContext.getComponent(ChatMultiServer.class, "chatMultiServer");
        LoginService loginService = applicationContext.getComponent(LoginService.class, "loginServiceImpl");
//        System.out.println(loginService);
        chatMultiServer.start(6666);
    }
}
