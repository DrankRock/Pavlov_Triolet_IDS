import java.rmi.*;

/**
 * RMI Interface "Hello"
 * @version 0.1
 * 
 */
public interface Server extends Remote {
    public void message(String user, String s) throws RemoteException;
    public int nextID()  throws RemoteException;
    public int connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException;
    public void disconnect(Info_itf_Impl infos) throws RemoteException;
}  
