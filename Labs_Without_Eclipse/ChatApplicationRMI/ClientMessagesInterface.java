import java.rmi.*;
import java.util.ArrayList;

/**
 * Interface of the ClientMessage, that is used by the server to send message to
 * the client.
 */
public interface ClientMessagesInterface extends Remote {
	public void displayMessage(String s) throws RemoteException;
	public void displayMessage(String user, String s) throws RemoteException;
	public void displayMessage(ArrayList<String> ss) throws RemoteException;
	public void displayMessageGreen(String s) throws RemoteException;
	public void run(Info_itf_Impl inf) throws RemoteException, NotBoundException;
	public void closingServer() throws RemoteException;
}  