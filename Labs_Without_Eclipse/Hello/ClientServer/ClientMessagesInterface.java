import java.rmi.*;

/**
 * RMI Interface "Hello"
 * @version 0.1
 * 
 */
public interface ClientMessagesInterface extends Remote {
	public void displayMessage(String s) throws RemoteException;
	public void displayMessage(String user, String s) throws RemoteException;
}  