import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Implementation of the client, this function is what is called when a client wishes to log in. 
 */
public class ClientImpl {
	private Server h;

	Info_itf_Impl infos; //current user informations
	ClientMessages messaging; // receiving messages from the server

	/**
	 * Constructor. It does minimal things, to be able to give it as an argument
	 * as soon as possible. Data can be changed later.
	 */
	public ClientImpl(String host, Server h) throws RemoteException, NotBoundException{
		this.h = h;
		this.infos = new Info_itf_Impl("", "");
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
		Tools.dprint("Continue with "+username+", "+password);
		this.connectionWithServer(username, password);
	}

	/**
	 * Tries to connect to the server using username and password from the connection GUI.
	 * If the password is incorrect, exits. Else, launches the chat GUI
	 * @param user username
	 * @param pass password
	 * @throws RemoteException Errors with RMI
	 * @throws NotBoundException Errors with RMI
	 */
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

	/**
	 * Convert client to string
	 */
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
	 * Main class, launch everything
	 * @param args Command line arguments. Nothing forces localhost
	 * @throws RemoteException RMI Connection issues
	 * @throws NotBoundException RMI Connection issues
	 */
	public static void main(String [] args) throws RemoteException, NotBoundException {
		String hst = null;
		if (args.length == 0) {
			hst = "localhost";
		} else if (args.length != 1){
			System.out.println("Usage: java HelloClient <rmiregistry host ex: localhost>");
			return;
		} else {
			hst = args[0];
		}
		// Get remote object reference
		Registry registry = LocateRegistry.getRegistry(hst); // get the registry of the host given in argument
		// Here, we will always use "localhost"
		Server h = (Server) registry.lookup("RunningServerPT1"); //get the server running on the RMI

		ClientImpl hc = new ClientImpl(hst, h);
		hc.run();
		// On shutdown of the client, send a disconnect notification to the server
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
					hc.disconnect();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
            }
        });
	}
}