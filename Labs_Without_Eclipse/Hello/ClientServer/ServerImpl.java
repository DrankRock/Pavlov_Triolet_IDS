import java.rmi.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import com.clientserver.*;

public  class ServerImpl implements Server {

	private String message;
	private int lastGivenID;
	public ArrayList<String> names;
	private HashMap<String, String> userToPass;
	private HashMap<String, Integer> userToID;
	private HashMap<String, Client> userToClientStub;
	public Random r = new Random();
 
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
	public int connect(String user, String pass) throws RemoteException{
		System.out.println("[CONNECT] - "+user+" : "+pass);
		if (userToPass.containsKey(user)){
			System.out.println("[CONNECT] - user exists");
			System.out.println("[CONNECT] - userToPass(user) = "+userToPass.get(user));
			if (userToPass.get(user).equals(pass)){
				System.out.println("[CONNECT] - "+user+" successfully connected.");
				return userToID.get(user);
			} else {
				System.out.println("[CONNECT] - incorrect password");
				return -1; //incorrect password
			}
		} else {
			System.out.println("[CONNECT] - create user");
			// create user
			userToPass.put(user, pass);
			int id = this.nextID();
			userToID.put(user, id);
			return id; //return id
		}
	}

	public void message(String s) throws RemoteException{
		// broadcast message
		Tools.dprint("Message sent : "+s);

		for (Client client : userToClientStub.values()) {
			client.recieveMessage(s);
			// ...
		}
		

	}

	public void register(Client c) throws RemoteException {
		Client c2 = (Client) c;
		Tools.dprint("user trying to register");
		Tools.dprint(c2.toString());
		Tools.dprint("Adding "+c2.getInfos().name+" to active users");
		userToClientStub.put(c.getInfos().getName(), c);
	}

	public void disconnect(Info_itf_Impl infos) throws RemoteException{
		Tools.dprint("Removing "+infos.name+" from active users");
		userToClientStub.remove(infos.getName());
	}
}

