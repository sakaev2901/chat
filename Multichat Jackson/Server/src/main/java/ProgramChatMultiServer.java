import context.ApplicationContext;
import context.ApplicationContextReflectionBased;
import servers.ChatMultiServer;

public class ProgramChatMultiServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContextReflectionBased();
        ChatMultiServer chatMultiServer = applicationContext.getComponent(ChatMultiServer.class, "chatMultiServer");
        chatMultiServer.start(6666);
    }
}
