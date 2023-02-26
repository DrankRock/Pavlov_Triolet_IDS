import java.rmi.*;

/**
 * Interface of the info class
 */
public interface Info_itf {
	/**
	 * Getter of the username
	 * @return a String containing the username 
	 * @throws RemoteException RMI Connection issues
	 */
	public String getName() throws RemoteException;
	/**
	 * Getter of the identifier. The identifier is a randomly generated string
	 * that makes it less likely that users spoof each other
	 * @return a String containing the identifier 
	 * @throws RemoteException RMI Connection issues
	 */
	public String getID() throws RemoteException;
}