import java.io.Serializable;
import java.rmi.*;

/**
 * Class containing two strings, to send infos about the client easily.
 */
public class Info_itf_Impl implements Info_itf, Serializable{
	/**
	 * username of the user
	 */
	private String name;
	/**
	 * identifier of the user to avoid spoofing and prove he really is the user
	 */
	private String ID;
	/**
	 * Constructor
	 * @param s username
	 * @param id identifier hash
	 * @throws RemoteException RMI Connection issues
	 */
	public Info_itf_Impl(String s, String id) throws RemoteException{ 
		name = s;
		ID = id;
	}
	/**
	 * getter of username
	 */
	public String getName() throws RemoteException{
		return name;
	}
	/**
	 * getter of identifier
	 */
	public String getID() throws RemoteException{
		return ID;
	}
	/**
	 * return string of the infos.
	 */
	public String toString(){
		return "(Infos_itf_Impl) "+name+", "+ID;
	}
}