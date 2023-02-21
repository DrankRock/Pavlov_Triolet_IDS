import java.rmi.*;

/**
 * RMI Interface "Client"
 * @version 0.1
 * 
 */
public interface Client extends Remote {
	public String sayHello(String s, int id)  throws RemoteException;
    public String sayHello(String s, Info_itf client)  throws RemoteException;
	public int giveInt()  throws RemoteException;
    public int nextID()  throws RemoteException;
}