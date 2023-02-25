import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientImpl {
	private Server h;
	private int myID;

	Info_itf_Impl infos;
	Thread listener;
	ClientMessages messaging;

	public ClientImpl(String host, Server h) throws RemoteException, NotBoundException{
		this.h = h;
		this.infos = new Info_itf_Impl("", "");
	}

	public void connectionWithServer(String user, String pass) throws RemoteException, NotBoundException{
		messaging = new ClientMessages(user);
		ClientMessagesInterface messaging_stub = (ClientMessagesInterface) UnicastRemoteObject.exportObject(messaging, 0);
		Tools.dprint("Launched messages stub");
		String connectionID = h.connect(user, pass, messaging_stub) ;
		if( connectionID == null){
			System.out.println("Incorrect Password.");
			System.exit(0);
		} else {
			infos = new Info_itf_Impl(user, connectionID);
		}
		messaging_stub.run(infos);
		h.notifyOfActiveGUI(infos);
	}
	/**
	 * Send message to server
	 * @param s String containing the message
	 * @throws RemoteException RMI connection issues
	 */
	public void sendMessage(String s) throws RemoteException{
		Tools.dprint("[INFO] - "+infos.getName()+" sending \""+s+"\"");
		h.message(infos, s);
	}

	/**
	 * Disconnect from the server (not currently used by this class)
	 * @throws RemoteException
	 */
	public void disconnect() throws RemoteException {
		h.disconnect(infos);
	}

	public String toString(){
		return infos.toString();
	}
	/**
	 * Disconnects then quits
	 * @throws RemoteException RMI Connection issues
	 */
	public void quit() throws RemoteException{
		this.disconnect();
		System.exit(0);
	}

	/**
	 * First step of the execution : the connection page
	 */
	public void run(){
		// open connexion page
		Tools.dprint("Call run");
		ConnexionClient cc = new ConnexionClient();
		cc.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				String username = cc.getUsername();
				String password = cc.getPassword();
				try {
					System.out.println(username+", "+password);
					continueRun(username, password);
				} catch (RemoteException | NotBoundException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * Second step of the execution : connect to the server
	 * @param username
	 * @param password
	 * @throws RemoteException RMI Connection issues
	 * @throws NotBoundException
	 */
	public void continueRun(String username, String password) throws RemoteException, NotBoundException{
		if (username == ""){
			throw new IllegalArgumentException("The entered Username is not valid");
		}
		System.out.println("Continue with "+username+", "+password);
		this.connectionWithServer(username, password);
	}

	public Info_itf_Impl getInfos(){
		return this.infos;
	}

	public static void main(String [] args) throws RemoteException, NotBoundException {
		if (args.length != 1) {
			System.out.println("Usage: java HelloClient <rmiregistry host ex: localhost>");
			return;
		}
		// Get remote object reference
		Registry registry = LocateRegistry.getRegistry(args[0]); // get the registry of the host given in argument
		// Here, we will always use "localhost"
		Server h = (Server) registry.lookup("RunningServerPT1"); //get the server running on the RMI

		ClientImpl hc = new ClientImpl(args[0], h);
		hc.run();
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
					hc.disconnect();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
            }
        });
		//ClientImpl cc = (ClientImpl) UnicastRemoteObject.exportObject(hc, 0);	

		//System.exit(1);
		//hc.run(args);
	}
}