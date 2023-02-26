import java.rmi.*;

/**
 * Server interface
 * 
 */
public interface Server extends Remote {
    /**
	 * Remotely called, for user to broadcast a message.
	 * infos are necessary to verify that the stored hash by the server is the same
	 * as the one given by the client.
	 * @param infos infos containing the username and identifier of the sender
	 * @param s the message to send
	 * @throws RemoteException RMI Connection issues
	 */
    public void message(Info_itf_Impl infos, String s) throws RemoteException;
    /**
	 * Remotely called function to try a user:pass.
	 * Returns a unique identifier if it works, else returns null.
	 * 
	 * Important security note : this is not secured. The communication between the server and the client
	 * can be caught by a MITM attack, and the passwords are sent in clear. 
	 * A document here shows ideas on how to implement it, but as it was not the focus, we did not try
	 * too much : 
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/rmi/socketfactory/index.html
	 * 
	 * @param user username of the user
	 * @param pass password of the user (in clear !)
	 * @param cmi ClientMessage stub to enable server->Client communication
	 * @throws RemoteException RMI Connection issues
     * @return String containing the unique identifier of this client
	 */
    public String connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException;
    /**
	 * disconnect user from the server
	 * @param infos infos containing the username and identifier of the sender
	 * @throws RemoteException RMI Connection issues
	 */
    public void disconnect(Info_itf_Impl infos) throws RemoteException;
    /**
	 * Start the GUI of the server 
	 * @param identifier server identifier
	 * @throws RemoteException RMI COnnection issues
	 * @throws NotBoundException RMI Connection issues
	 */
    public void startGUI(String identifier) throws RemoteException, NotBoundException;
    /**
	 * Load userData from the file, to an arraylist
	 * @param s Filename to load from
	 * @throws RemoteException RMI Connection issues
	 */
    public void loadUsers(String s) throws RemoteException;
    /**
	 * Load chat history from the file, to an arraylist
	 * @param s Filename to load from
     * @throws RemoteException RMI COnnection issues
	 */
    public void loadHistory(String s) throws RemoteException;
    /**
	 * Remotely called, by client, to notify the server that it has launched the gui
	 * @param inf username and identifier of a user
	 * @throws RemoteException RMI Connection issues
	 */
    public void notifyOfActiveGUI(Info_itf_Impl inf) throws RemoteException;
    /**
	 * Start logs backup every 30 seconds 
	 * @param identifier HelloServer identifier to avoid server spoofing
	 * @throws RemoteException RMI Connection issues
	 */
    public void startLogs(String identifier) throws RemoteException;
}  
