import java.rmi.*;
import java.rmi.registry.*;

/**
 * Class HelloClient, connects to the server and executes "sayHello", then returns 
 * 
 */
public class HelloClient implements Client{
  public static void main(String [] args) {
	
	try {
		if (args.length < 1) {
			System.out.println("Usage: java HelloClient <rmiregistry host ex: localhost>");
			return;
		}

	String host = args[0];

	// Get remote object reference
	Registry registry = LocateRegistry.getRegistry(host); // get the registry of the host given in argument
	// Here, we will always use "localhost"
	Hello h = (Hello) registry.lookup("HelloService"); //get the server running on the RMI

	// Remote method invocation
	String s = "caca";
	String res = h.sayHello(s, 0); // execute command 
	int resTo = h.giveInt(); // this is a call to the server !
	System.out.println(res+" int is : "+resTo);

	} catch (Exception e)  {
		System.err.println("Error on client: " + e);
	}
  }
}