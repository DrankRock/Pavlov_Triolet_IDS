
import java.rmi.*;

public  class HelloImpl implements Hello {

	private String message;
	private int myInt;
 
	public HelloImpl(String s, int i) {
		message = s ;
		myInt = i;
	}

	public String sayHello(String s) throws RemoteException {
		return message+", Client said "+s;
	}

	public int giveInt() throws RemoteException {
		return myInt;
	}
}

