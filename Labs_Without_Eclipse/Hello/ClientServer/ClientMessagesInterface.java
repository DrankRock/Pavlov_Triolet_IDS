import java.lang.ProcessHandle.Info;
import java.rmi.*;
import java.util.ArrayList;

/**
 * RMI Interface "Hello"
 * @version 0.1
 * 
 */
public interface ClientMessagesInterface extends Remote {
	public void displayMessage(String s) throws RemoteException;
	public void displayMessage(String user, String s) throws RemoteException;
	public void displayMessage(ArrayList<String> ss) throws RemoteException;
	public void run(Info_itf_Impl inf) throws RemoteException, NotBoundException;
	public void closingServer() throws RemoteException;
}  