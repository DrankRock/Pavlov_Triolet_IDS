import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void register(ChatClient client) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
}