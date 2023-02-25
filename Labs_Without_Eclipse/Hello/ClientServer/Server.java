import java.rmi.*;

/**
 * Server interface
 * 
 */
public interface Server extends Remote {
    public void message(Info_itf_Impl infos, String s) throws RemoteException;
    public String connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException;
    public void disconnect(Info_itf_Impl infos) throws RemoteException;
    public void startGUI(String identifier) throws RemoteException, NotBoundException;
    public void loadUsers(String s) throws RemoteException;
    public void loadHistory(String s) throws RemoteException;
    public void notifyOfActiveGUI(Info_itf_Impl inf) throws RemoteException;
    public void startLogs() throws RemoteException;
}  
