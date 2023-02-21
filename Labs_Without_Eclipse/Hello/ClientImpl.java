
import java.rmi.*;

public  class HelloImpl implements Hello {

	private String message;
	private int lastGivenID;
 
	public HelloImpl(String s, int i) {
		message = s ;
		lastGivenID = 0;
	}

	public String sayHello(String s, int id) throws RemoteException {
		//System.out.println("Connection initiated by "+id); // Solution 1	

		return message+", Client said "+s;
	}
	public String sayHello(String s, Info_itf inf) throws RemoteException {
		System.out.println("Connection initiated by "+inf.getID()); // Solution 2
		
		return message+", Client said "+s;
	}

	public int giveInt() throws RemoteException {
		return 0;
	}
	public int nextID() throws RemoteException {
		return lastGivenID++;
	}
}

