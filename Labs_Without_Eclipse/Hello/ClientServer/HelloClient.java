
import java.rmi.*; 
import java.rmi.server.*; 
import java.rmi.registry.*;
import java.util.Scanner; 

public class HelloClient {

  public static void  main(String [] args) {
	  try {
		if (args.length != 3) {
			System.out.println("Usage: java HelloClient <rmiregistry host ex: localhost> <username> <password>");
			return;
		}
		// Get remote object reference
		Registry registry = LocateRegistry.getRegistry(args[0]); // get the registry of the host given in argument
		// Here, we will always use "localhost"
		Server h = (Server) registry.lookup("HelloService"); //get the server running on the RMI
		  // Create a Hello remote object
	    ClientImpl c = new ClientImpl (args[1], args[2], args[0], h);	
	    //Client c_stub = (Client) UnicastRemoteObject.exportObject(c, 0);

		h.register(c);

		Scanner userInput = new Scanner(System.in);
		System.out.println ("Client ready. 'quit' to quit. Anything else to send it");
		while(true){
			String action = userInput.nextLine();
			switch(action){
				case "quit":
					c.disconnect();
					userInput.close();
					System.exit(0);
					break;
				default :
					c.sendMessage(action);
					break;
			}
		}

	    // Register the remote object in RMI registry with a given identifier
	    //Registry registry= LocateRegistry.getRegistry(); 
	    //registry.bind("HelloService", h_stub);

	  } catch (Exception e) {
		  System.err.println("Error on client :" + e) ;
		  e.printStackTrace();
		  
	  }
  }
}
