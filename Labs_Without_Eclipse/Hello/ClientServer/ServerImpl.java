import java.rmi.*;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * NOTE :  ID is actually useless I think, because everything is done through a unique username
 * I make it work, and then i'll delete if necessary
 */
public  class ServerImpl implements Server {

	private int lastGivenID;
	private HashMap<String, String> userToPass;
	private HashMap<String, Integer> userToID;
	private HashMap<String, ClientMessagesInterface> userToClientStub;
	private ArrayList<String> chatHistory;
	public SecureRandom r = new SecureRandom();
	FileLoader history;
	FileLoader userData;

	ServerApp gui;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss:SSS:AAAA");

	private String callerServerIDHash;
 
	public ServerImpl(String s, int i){
		callerServerIDHash = s ;
		lastGivenID = 0;
		userToPass = new HashMap<>();
		userToID = new HashMap<>();
		userToClientStub = new HashMap<>();
		history = new FileLoader(".history");
		userData = new FileLoader(".userdata");
		
	}

	public void startGUI(String s) throws RemoteException, NotBoundException{
		if (s.equals(callerServerIDHash)){
			gui = new ServerApp();
			gui.run();
		}
	}

	public int nextID() throws RemoteException {
		return lastGivenID++;
	}

	// returns the id of the user
	public int connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException{
		int ret = -1;
		log("[CONNECT] ("+user+") - "+user+" : "+pass);

		if (userToPass.containsKey(user)){
			log("[CONNECT] ("+user+") - user exists");
			log("[CONNECT] ("+user+") - userToPass(user) = "+userToPass.get(user));
		
			if (arePasswordsEqual(userToPass.get(user), pass)){
				log("[CONNECT] ("+user+") - "+user+" successfully connected.");
				ret = userToID.get(user);
				cmi.displayMessage("Welcome back, "+user+".");
			} else {
				log("[CONNECT] ("+user+") - incorrect password");
			}
		} else {
			log("[CONNECT] ("+user+") - create user");
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
			giveHistoryToUser(cmi);
		}
		return ret;
	}

	private void giveHistoryToUser(ClientMessagesInterface cmi){

	}

	public boolean arePasswordsEqual(String saltAndPass, String passwd){
		String[] spl = saltAndPass.split(":");
		String hashed = Security.encode(passwd, spl[0]);
		return spl[1].equals(hashed);
	}

	public void message(String user, String s) throws RemoteException{
		// broadcast message
		log("[INFO] - Message recieved from <"+user+">");

		for (ClientMessagesInterface client : userToClientStub.values()) {
			client.displayMessage(user, s);
		}
		

	}

	public void disconnect(Info_itf_Impl infos) throws RemoteException{
		log("[DISCONNECT] - Removing "+infos.name+" from active users");
		userToClientStub.remove(infos.getName());
	}

	public void loadUsers(String s) throws RemoteException{
		if (s.equals(callerServerIDHash)){
			ArrayList<String> lines = userData.readLines();
			log("[SETUP] - Loading Users : ");
			for (String line : lines){
				try {
					UserData current = Security.stringToUserdata(line);
					userToPass.put(current.getUser(), current.getSalt()+":"+current.getPassword());
					userToID.put(current.getUser(), this.nextID());
					log("[SETUP] | - Loaded : "+current.getUser());
				} catch (NullPointerException npe){
					log("[SETUP] | - line :'"+line+"' Contains no user:salt:password");
				}
			}
		}
	}

	public void loadHistory(String s){
		if (s.equals(callerServerIDHash)){
			chatHistory = history.readLines();
			log("[SETUP] - HISTORY LOADED ("+chatHistory.size()+" lines)");
		}

	}

	private void log(String s){
		gui.log("("+dtf.format(LocalTime.now())+") "+s);
	}

}

