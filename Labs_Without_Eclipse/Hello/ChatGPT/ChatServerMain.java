import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ChatServerMain {
    public static void main(String[] args) throws Exception {
        LocateRegistry.getRegistry();
        ChatServer server = new ChatServerImpl();
        Naming.rebind("ChatServer", server);
        System.out.println("Server started");
    }
}