import java.rmi.*;

public class ClientMessages implements ClientMessagesInterface {

    public void displayMessage(String s) throws RemoteException{
        System.out.println("[MSG] - "+s);
    }
}