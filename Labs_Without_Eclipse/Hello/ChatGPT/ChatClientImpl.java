import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
    private ChatServer server;

    public ChatClientImpl(ChatServer server) throws RemoteException {
        this.server = server;
    }

    public void receiveMessage(String message) throws RemoteException {
        System.out.println("Received message: " + message);
    }

    public void start() throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String message = scanner.nextLine();
            server.broadcastMessage(message);
        }
    }
}