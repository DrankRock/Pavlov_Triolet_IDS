import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class HelloClient, connects to the server and executes "sayHello", then returns 
 * 
 */
public class ClientImpl extends UnicastRemoteObject implements Client{
	private Server h;
	private int myID;
	Info_itf_Impl infos;

	public ClientImpl(String user, String pass, String host, Server h) throws RemoteException, NotBoundException{
		this.h = h;
		Tools.dprint("Initializing user : "+user);
		myID = this.h.connect(user, pass);
		if (myID == -1){
			System.out.println("Incorrect Password .. ");
			System.exit(0);
		}
		Tools.dprint("Connected. Given id : "+myID);
		this.infos = new Info_itf_Impl(user, myID);
		Tools.dprint("Given infos : "+this.infos);
	}

	public void sendMessage(String s) throws RemoteException{
		// send message to server
		Tools.dprint("[INFO] - "+infos.name+" sending \""+s+"\"");
		h.message(s);
	}

	@Override
	public void RegisterToServer(Client c) throws RemoteException {
		System.out.println("Register reached");
		h.register(c);
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

	public String toString(){
		return infos.toString()+", "+myID;
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