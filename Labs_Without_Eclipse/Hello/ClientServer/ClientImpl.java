import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Class HelloClient, connects to the server and executes "sayHello", then returns 
 * 
 */
public class ClientImpl {
	private Server h;
	private int myID;
	Info_itf_Impl infos;
	Thread listener;
	ClientMessages messaging;

	public ClientImpl(String user, String pass, String host, Server h) throws RemoteException, NotBoundException{
		Tools.dprint("Create clientimpl");
		this.h = h;
		this.messaging = new ClientMessages(user);
		ClientMessagesInterface messaging_stub = (ClientMessagesInterface) UnicastRemoteObject.exportObject(messaging, 0);	
		myID = this.h.connect(user, pass, messaging_stub);
		if (myID == -1){
			System.out.println("Incorrect Password .. ");
			System.exit(0);
		}
		Tools.dprint("Connected. Given id : "+myID);
		this.infos = new Info_itf_Impl(user, myID);
		Tools.dprint("Given infos : "+this.infos);
		
		//messaging.displayMessage("Initializing user : "+user);
	}

	public void sendMessage(String s) throws RemoteException{
		// send message to server
		Tools.dprint("[INFO] - "+infos.name+" sending \""+s+"\"");
		h.message(infos.getName(), s);
	}

	public void recieveMessage(String s) {
		System.out.println("[MSG] : "+s);
	}

	public void disconnect() throws RemoteException {
		h.disconnect(infos);
	}

	public String toString(){
		return infos.toString()+", "+myID;
	}
	public void quit() throws RemoteException{
		this.disconnect();
		//this.listener.interrupt();
		System.exit(0);
	}
	public void launchListener(){
		/*
		listener = new Thread(){
			public void run(){
				while(!Thread.interrupted()){
					
				}
			}
		  };
		
		  listener.start();*/
	}

	public void run() throws RemoteException{
		Scanner scanner = new Scanner(System.in);
		String s;
		while (scanner.hasNextLine()){
			s = scanner.nextLine();
			if (s.equals("quit")){
				scanner.close();
				this.quit();
			} else {
				sendMessage(s);
			}
		}
		scanner.close();
	}

	public static void main(String [] args) throws RemoteException, NotBoundException {
		if (args.length != 3) {
			System.out.println("Usage: java HelloClient <rmiregistry host ex: localhost> <username> <password>");
			return;
		}
		// Get remote object reference
		Registry registry = LocateRegistry.getRegistry(args[0]); // get the registry of the host given in argument
		// Here, we will always use "localhost"
		Server h = (Server) registry.lookup("RunningServerPT1"); //get the server running on the RMI
		ClientImpl hc = new ClientImpl(args[1], args[2], args[0], h);

		//ClientImpl cc = (ClientImpl) UnicastRemoteObject.exportObject(hc, 0);	

		hc.run();
		System.exit(1);
		//hc.run(args);
	}
}