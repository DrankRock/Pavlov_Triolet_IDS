import java.rmi.server.*; 
import java.rmi.registry.*;

/**
 * Server class, launching the server stub, and initializing it
 */
public class HelloServer {

  public static void  main(String [] args) {
	  try {
		  // Create a remote object
		String sessionServerHash = Security.encode("RunningServerPT1", Security.bytesToHex(Security.getSalt()));
		// The above --^ hash is here to avoid anything else than this class to give admin orders to the server stub
		// This is not currently used, but it has the spirit !

	    ServerImpl h = new ServerImpl (sessionServerHash);
	    Server h_stub = (Server) UnicastRemoteObject.exportObject(h, 0);	

	    // Register the remote object in RMI registry with a given identifier
	    Registry registry= LocateRegistry.getRegistry(); 
	    registry.bind("RunningServerPT1", h_stub);
		h_stub.startGUI(sessionServerHash); // launch server gui
		h_stub.startLogs(); // launch logs backup to logfile
		h_stub.loadUsers(sessionServerHash); // launch user/pass loading 
		h_stub.loadHistory(sessionServerHash); // launch history loading + history backup
		
	    System.out.println ("Server ready");

	  } catch (Exception e) {
		  System.err.println("Error on server :" + e) ;
		  e.printStackTrace();
	  }
  }
}
