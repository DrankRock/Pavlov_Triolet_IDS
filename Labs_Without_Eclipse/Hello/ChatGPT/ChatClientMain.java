import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ChatClientMain {
    public static void main(String[] args) throws Exception {
        ChatServer server = (ChatServer) Naming.lookup("rmi://localhost/ChatServer");
        ChatClientImpl client = new ChatClientImpl(server);
        server.register(client);
        client.start();
    }
}