
import java.rmi.server.*; 
import java.rmi.registry.*;

public class HelloServer {

  public static void  main(String [] args) {
	  try {
		  // Create a Hello remote object
		String sessionServerHash = Security.encode("RunningServerPT1", Security.bytesToHex(Security.getSalt()));
	    ServerImpl h = new ServerImpl (sessionServerHash, 42);
	    Server h_stub = (Server) UnicastRemoteObject.exportObject(h, 0);	

	    // Register the remote object in RMI registry with a given identifier
	    Registry registry= LocateRegistry.getRegistry(); 
	    registry.bind("RunningServerPT1", h_stub);
		h_stub.startGUI(sessionServerHash); // launch server gui
		h_stub.loadUsers(sessionServerHash);
		h_stub.loadHistory(sessionServerHash);
	    System.out.println ("Server ready");

	  } catch (Exception e) {
		  System.err.println("Error on server :" + e) ;
		  e.printStackTrace();
	  }
  }
}
