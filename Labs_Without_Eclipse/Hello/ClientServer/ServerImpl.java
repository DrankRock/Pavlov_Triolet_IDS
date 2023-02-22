// Importing the RMI package.
import java.io.File;
import java.rmi.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

public  class ServerImpl implements Server {

	private String message;
	private int lastGivenID;
	public ArrayList<String> names;
	private HashMap<String, String> userToPass;
	private HashMap<String, Integer> userToID;
	private HashMap<String, ClientMessagesInterface> userToClientStub;
	public Random r = new Random();
	FileLoader history;
	FileLoader userData;
 
	public ServerImpl(String s, int i) {
		message = s ;
		lastGivenID = 0;
		names = new ArrayList<String>() {{
		add("Wade");
		add("Dave");
		add("Seth");
		add("Ivan");
		add("Riley");
		add("Gilbert");
		add("Jorge");
		add("Dan");
		add("Olivia");
		add("Emma");
		add("Charlotte");
		add("Amelia");
		add("Ava");
		add("Sophia");
		add("Isabella");
		add("Mia");
	}};
	userToPass = new HashMap<>();
	userToID = new HashMap<>();
	userToClientStub = new HashMap<>();
	history = new FileLoader(".history");
	userData = new FileLoader(".userdata");
	}

	public String sayHello(String s, int id) throws RemoteException {
		//System.out.println("Connection initiated by "+id); // Solution 1	

		return message+", Client said "+s;
	}
	public String sayHello(String s, Info_itf inf) throws RemoteException {
		System.out.println("Connection initiated by "+inf.getID()); // Solution 2
		
		return "Hello from the server, "+inf.getName();
	}

	public int giveInt() throws RemoteException {
		return 0;
	}
	public int nextID() throws RemoteException {
		return lastGivenID++;
	}
	public String getName() throws RemoteException {
		return this.names.get(r.nextInt(names.size() - 1));
	}

	// returns the id of the user
	public int connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException{
		int ret = -1;
		System.out.println("[CONNECT] - "+user+" : "+pass);
		if (userToPass.containsKey(user)){
			System.out.println("[CONNECT] - user exists");
			System.out.println("[CONNECT] - userToPass(user) = "+userToPass.get(user));
			if (userToPass.get(user).equals(pass)){
				System.out.println("[CONNECT] - "+user+" successfully connected.");
				ret = userToID.get(user);
			} else {
				System.out.println("[CONNECT] - incorrect password");
			}
		} else {
			System.out.println("[CONNECT] - create user");
			// create user
			userToPass.put(user, pass);
			int id = this.nextID();
			userToID.put(user, id);
			ret = id; //return id
		}
		if (ret != -1 ){
			userToClientStub.put(user, cmi);
		}
		return ret;
	}

	public void message(String user, String s) throws RemoteException{
		// broadcast message
		Tools.dprint("Message sent : "+s);

		for (ClientMessagesInterface client : userToClientStub.values()) {
			client.displayMessage(user, s);
			// ...
		}
		

	}

	public void disconnect(Info_itf_Impl infos) throws RemoteException{
		Tools.dprint("Removing "+infos.name+" from active users");
		userToClientStub.remove(infos.getName());
	}

}

