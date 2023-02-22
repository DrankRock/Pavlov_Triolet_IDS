// Importing the RMI package.
import java.io.File;
import java.rmi.*;
import java.security.SecureRandom;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * NOTE :  ID is actually useless I think, because everything is done through a unique username
 * I make it work, and then i'll delete if necessary
 */
public  class ServerImpl implements Server {

	private String message;
	private int lastGivenID;
	private HashMap<String, String> userToPass;
	private HashMap<String, Integer> userToID;
	private HashMap<String, ClientMessagesInterface> userToClientStub;
	public SecureRandom r = new SecureRandom();
	FileLoader history;
	FileLoader userData;
 
	public ServerImpl(String s, int i) {
		message = s ;
		lastGivenID = 0;
		userToPass = new HashMap<>();
		userToID = new HashMap<>();
		userToClientStub = new HashMap<>();
		history = new FileLoader(".history");
		userData = new FileLoader(".userdata");
		try {
			loadUsers();
			loadHistory();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int nextID() throws RemoteException {
		return lastGivenID++;
	}

	// returns the id of the user
	public int connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException{
		int ret = -1;
		Tools.dprint("[CONNECT] - "+user+" : "+pass);

		if (userToPass.containsKey(user)){
			Tools.dprint("[CONNECT] - user exists");
			Tools.dprint("[CONNECT] - userToPass(user) = "+userToPass.get(user));
		
			if (arePasswordsEqual(userToPass.get(user), pass)){
				Tools.dprint("[CONNECT] - "+user+" successfully connected.");
				ret = userToID.get(user);
				cmi.displayMessage("Welcome back, "+user+".");
			} else {
				Tools.dprint("[CONNECT] - incorrect password");
			}
		} else {
			Tools.dprint("[CONNECT] - create user");
			// create user
			String salt = Security.bytesToHex(Security.getSalt());
			String hashedPwd = Security.encode(pass, salt);

			userToPass.put(user, salt+":"+hashedPwd);
			userData.addLine(""+user+":"+salt+":"+hashedPwd); // add user to file
			int id = this.nextID();
			userToID.put(user, id);
			ret = id; //return id
		}
		if (ret != -1 ){
			userToClientStub.put(user, cmi);
		}
		return ret;
	}

	public boolean arePasswordsEqual(String saltAndPass, String passwd){
		String[] spl = saltAndPass.split(":");
		String hashed = Security.encode(passwd, spl[0]);
		return spl[1].equals(hashed);
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

	private void loadUsers() throws RemoteException{
		ArrayList<String> lines = userData.readLines();
		for (String line : lines){
			try {
				UserData current = Security.stringToUserdata(line);
				userToPass.put(current.getUser(), current.getSalt()+":"+current.getPassword());
				userToID.put(current.getUser(), this.nextID());
			} catch (NullPointerException npe){
				Tools.dprint("line :'"+line+"'\nContains no user:salt:password");
			}
		}
	}

	private void loadHistory(){
		ArrayList<String> lines = history.readLines();

	}

}

