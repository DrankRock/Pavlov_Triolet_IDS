import java.rmi.*;

/**
 * RMI Interface "Hello"
 * @version 0.1
 * 
 */
public interface Client extends Remote{
    public void disconnect() throws RemoteException;
    public void sendMessage(String s) throws RemoteException;
    public void RegisterToServer(Client c) throws RemoteException;
    public void recieveMessage(String s) throws RemoteException;
    public Info_itf_Impl getInfos() throws RemoteException;
}  
