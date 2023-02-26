import java.rmi.*;

/**
 * Interface of the info class
 */
public interface Info_itf {
		public String getName() throws RemoteException;
		public String getID() throws RemoteException;
}