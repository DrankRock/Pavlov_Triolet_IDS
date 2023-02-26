import java.rmi.*;
import java.util.ArrayList;

/**
 * Interface of the ClientMessage, that is used by the server to send message to
 * the client.
 */
public interface ClientMessagesInterface extends Remote {
	    /**
     * run the gui, needs to be outside of the constructor, because the info
     * is incomplete until connection is confirmed
     * @param inf info of the user launching the gui
     * @throws RemoteException RMI Connection issues
	 * @throws NotBoundException RMI Connection issues
     */
    public void run(Info_itf_Impl inf) throws RemoteException, NotBoundException;

    /**
     * Display message inside gui
     * @param s string to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessage(String s) throws RemoteException;

    /**
     * Display message inside gui, but green
     * @param s string to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessageGreen(String s) throws RemoteException;

    /**
     * Display message with user name
     * @param user Username of the sender 
     * @param s string to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessage(String user, String s) throws RemoteException;

    /**
     * Display multiple messages (used to load history)
     * @param ss ArraList containing all strings to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessage(ArrayList<String> ss) throws RemoteException;

    /**
     * Called on closing of the server, to warn the user.
     * @throws RemoteException RMI Connection issues
     */
    public void closingServer() throws RemoteException;
}  