import java.rmi.*;

/**
 * RMI Interface "Hello"
 * @version 0.1
 * 
 */
public interface Server extends Remote {
	public String sayHello(String s, int id)  throws RemoteException;
    public String sayHello(String s, Info_itf client)  throws RemoteException;
    public void message(String s) throws RemoteException;
	public int giveInt()  throws RemoteException;
    public int nextID()  throws RemoteException;
    public String getName() throws RemoteException;
    public int connect(String user, String pass) throws RemoteException;
    public void register(Client c) throws RemoteException;
    public void disconnect(Info_itf_Impl infos) throws RemoteException;
}  
