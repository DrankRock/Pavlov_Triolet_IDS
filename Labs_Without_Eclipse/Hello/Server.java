
import java.rmi.*; 
import java.rmi.server.*; 
import java.rmi.registry.*;

public class Server {

  public static void  main(String [] args) {
	  try {
		  // Create a Hello remote object
	    CleintImpl h = new ClientImpl ("Hello world !", 42);
	    Hello h_stub = (Hello) UnicastRemoteObject.exportObject(h, 0);

	    // Register the remote object in RMI registry with a given identifier
	    Registry registry= LocateRegistry.getRegistry(); 
	    registry.bind("HelloService", h_stub);

	    System.out.println ("Server ready");

	  } catch (Exception e) {
		  System.err.println("Error on server :" + e) ;
		  e.printStackTrace();
	  }
  }
}
