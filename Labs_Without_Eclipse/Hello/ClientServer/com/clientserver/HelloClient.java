package com.clientserver;

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
		  // Create a Hello remote object
	    ClientImpl c = new ClientImpl (args[1], args[2], args[0]);
	    Client c_stub = (Client) UnicastRemoteObject.exportObject(c, 0);

		c.RegisterToServer(c_stub); // add to active clients

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
