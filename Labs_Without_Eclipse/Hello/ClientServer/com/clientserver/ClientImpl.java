package com.clientserver;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class HelloClient, connects to the server and executes "sayHello", then returns 
 * 
 */
public class ClientImpl implements Client{
	private Server h;
	private int myID;
	Info_itf_Impl infos;

	public ClientImpl(String user, String pass, String host) throws RemoteException, NotBoundException{
		// Get remote object reference
		Registry registry = LocateRegistry.getRegistry(host); // get the registry of the host given in argument
		// Here, we will always use "localhost"
		this.h = (Server) registry.lookup("HelloService"); //get the server running on the RMI
		this.h.connect(user, pass);
	}

	public void connect(String user, String pass) throws RemoteException{
		int myID = h.connect(user, pass);
		if (myID == -1){
			System.out.println("Incorrect Password .. ");
			System.exit(0);
		}
		this.myID = myID;
		this.infos = new Info_itf_Impl(user, myID);
	}

	public void sendMessage(String s) throws RemoteException{
		// send message to server
		Tools.dprint("[INFO] - "+infos.name+" sending \""+s+"\"");
		h.message(s);
	}

	@Override
	public void RegisterToServer(Client c) throws RemoteException {
		
		
	}

	@Override
	public Info_itf_Impl getInfos() throws RemoteException {
		return this.infos;
	}

	@Override
	public void recieveMessage(String s) throws RemoteException {
		System.out.println("[MSG] : "+s);
	}

	@Override
	public void disconnect() throws RemoteException {
		h.disconnect(infos);
	}

	/*public void run(String [] args){
		try {

		String host = args[0];
		String name = args[1];
		String pass = args[2];

		

		this.connect(name, pass);
		this.infos = new Info_itf_Impl(name, myID);

		String myName = h.getName();

		System.exit(0);

		} catch (Exception e)  {
			System.err.println("Error on client: " + e);
		}
	} 
  public static void main(String [] args) {
  		if (args.length != 3) {
				System.out.println("Usage: java HelloClient <rmiregistry host ex: localhost> <username> <password>");
				return;
			}
  	//ClientImpl hc = new ClientImpl();
  	//hc.run(args);*/
}