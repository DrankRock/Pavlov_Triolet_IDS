
import java.rmi.*;

public interface Info_itf {
		public String getName() throws RemoteException;
		public int getID() throws RemoteException;
}