import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<ChatClient> clients;

    public ChatServerImpl() throws RemoteException {
        clients = new ArrayList<>();
    }

    public void register(ChatClient client) throws RemoteException {
        clients.add(client);
        System.out.println("New client registered: " + client);
    }

    public void broadcastMessage(String message) throws RemoteException {
        System.out.println("Broadcasting message: " + message);
        for (ChatClient client : clients) {
            client.receiveMessage(message);
        }
    }
}