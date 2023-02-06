import java.rmi.*;

/**
 * RMI Interface "Hello"
 * @version 0.1
 * 
 */
public interface Hello extends Remote {
	public String sayHello(String s)  throws RemoteException;
	public int giveInt()  throws RemoteException;
}
