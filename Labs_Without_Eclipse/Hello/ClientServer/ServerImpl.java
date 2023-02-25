import java.rmi.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Server Class, doing all the server skulduggery and creating links between all the users.
 * 
 */
public  class ServerImpl implements Server {

	private HashMap<String, String> userToPass; //user to password hash
	private HashMap<String, String> userToID; //user to identifier
	private HashMap<String, ClientMessagesInterface> userToClientStub; //user to messaging stub
	private ArrayList<String> chatHistory; // history of all messages
	private ArrayList<String> bufferHistory; // buffer to backup all the messages at once
	private ArrayList<String> bufferLog; // buffer to backup all the logs at once
	private ArrayList<String> activeUsers; // list of active users. Not currently used, but had the 
	// intent to send private messages
	public SecureRandom r = new SecureRandom();
	FileLoader history;
	FileLoader userData;
	FileLoader logFile; 

	ServerApp gui;

	// Format to have precise datetime in the logs
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss:SSS:AAAA");

	private String callerServerIDHash; // to identify the server

	// these are used to schedule logs backup every 
	ScheduledExecutorService executorServiceHistory; // 2 minutes
	ScheduledExecutorService executorServiceLogs; // 30 seconds

 
	/**
	 * Constructor
	 * @param s server hash id
	 */
	public ServerImpl(String s){
		callerServerIDHash = s ;
		userToPass = new HashMap<>();
		userToID = new HashMap<>();
		userToClientStub = new HashMap<>();
		activeUsers = new ArrayList<>();
		history = new FileLoader(".history");
		bufferHistory = new ArrayList<>();
		bufferLog = new ArrayList<>();
		userData = new FileLoader(".userdata");
		logFile = new FileLoader(".logile");
	}

	/**
	 * Start the GUI of the server 
	 * @param s server identifier
	 */
	public void startGUI(String s) throws RemoteException, NotBoundException{
		if (s.equals(callerServerIDHash)){
			gui = new ServerApp();
			gui.run();
			// On server gui close, backup history, logs and exit all the clients.
			gui.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						Tools.dprint("Exit all users gui");
						backupHistory();
						backupLog();
						exitAll();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Backup the history of chats.
	 * not Synchronized because addline is critical and already synchronized
	 */
	private void backupHistory(){
		ArrayList<String> histo = new ArrayList<>(bufferHistory);
		bufferHistory.clear();
		for (String line : histo){
			history.addLine(line);
		}
		log("[INFO] - BACK UP HISTORY DONE, "+histo.size()+" entries added.");
	}

	/**
	 * Backup the history of server logs.
	 * not Synchronized because addline is critical and already synchronized
	 */
	private void backupLog(){
		ArrayList<String> logs = new ArrayList<>(bufferLog);
		bufferLog.clear();
		for (String line : logs){
			logFile.addLine(line);
		}
		log("[INFO] - BACK UP LOGFILE DONE, "+logs.size()+" entries added.");
	}

	/**
	 * Remotely called function to try a user:pass.
	 * Returns a unique identifier if it works, else returns null.
	 * 
	 * Important security note : this is not secured. The communication between the server and the client
	 * can be caught by a MITM attack, and the passwords are sent in clear. 
	 * A document here shows ideas on how to implement it, but as it was not the focus, we did not try
	 * too much : 
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/rmi/socketfactory/index.html
	 */
	public String connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException{
		String ret = null;
		log("[CONNECT] ("+user+") - "+user);

		if (userToPass.containsKey(user)){
			log("[CONNECT] ("+user+") - user exists");
		
			if (arePasswordsEqual(userToPass.get(user), pass)){
				log("[CONNECT] ("+user+") - "+user+" successfully connected.");
				ret = Tools.randomString(8); //unique identifier 
				//(unique as in 256^8 possibilities) 
				//(unique as in 18,446,744,073,709,551,616 possibilities)
				// to put this in a perspective, if you count from 0 to 256^8 with one number
				// every second, it would take you about 42 times the age of the universe
				// Yeah that's quite unique
				// .. 
				// Huh, 42 ?

				// Please note that it's not possible to say "18 quintillion 446 quadrillion 744 trillion 73 billion 709 million 551 thousand 616" in one second.
				// It takes me a bit less than 10 seconds.
			} else {
				log("[CONNECT] ("+user+") - incorrect password");
			}
		} else {
			log("[CONNECT] ("+user+") - create user");
			// Create a new user
			String salt = Security.bytesToHex(Security.getSalt());
			String hashedPwd = Security.encode(pass, salt);

			userToPass.put(user, salt+":"+hashedPwd);
			userData.addLine(""+user+":"+salt+":"+hashedPwd); // add user to file

			ret = Tools.randomString(8); // same as above. Pretty unique.
			// Did you know that a grain of sugar is around 0.4 mm wide ? 
			// (nah, not calculating this)
		}
		if (ret != null){
			userToID.put(user, ret);
			userToClientStub.put(user, cmi);
		}
		return ret;
	}

	/**
	 * Remotely called, by client, to notify the server that it has launched the gui
	 */
	public void notifyOfActiveGUI(Info_itf_Impl inf) throws RemoteException{
		if (inf.getID().equals(userToID.get(inf.getName()))){
			// if id corresponds to the one given to the user
			activeUsers.add(inf.getName());
			giveHistoryToUser(inf.getName());
			userToClientStub.get(inf.getName()).displayMessageGreen("Welcome here, "+inf.getName()+"!");
		}
	}

	/**
	 * Load history inside user's chat
	 * @param user user, identifier
	 * @throws RemoteException RMI connection issues
	 */
	private void giveHistoryToUser(String user) throws RemoteException{

		userToClientStub.get(user).displayMessage(this.chatHistory);
	}

	/**
	 * Check if string is equal to the hashed stored data
	 * @param saltAndPass password salt and password 
	 * @param passwd clear password
	 * @return boolean, true if they are equals, else false
	 */
	public boolean arePasswordsEqual(String saltAndPass, String passwd){
		String[] spl = saltAndPass.split(":");
		String hashed = Security.encode(passwd, spl[0]);
		return spl[1].equals(hashed);
	}

	/**
	 * Remotely called, for user to broadcast a message.
	 * infos are necessary to verify that the stored hash by the server is the same
	 * as the one given by the client.
	 */
	public void message(Info_itf_Impl infos, String s) throws RemoteException{
		// broadcast message
		
		log("[INFO] - Message received from <"+infos.getName()+">\n    Added to history !");
		chatHistory.add(infos.getName()+": "+s);
		bufferHistory.add(infos.getName()+": "+s);
		for (ClientMessagesInterface client : userToClientStub.values()) {
			client.displayMessage(infos.getName(), s);
		}
	}

	/**
	 * disconnect user from the server
	 */
	public void disconnect(Info_itf_Impl infos) throws RemoteException{
		log("[DISCONNECT] - Removing "+infos.getName()+" from active users");
		userToClientStub.remove(infos.getName());
		activeUsers.remove(infos.getName());
	}

	/*
	 *  LOADERS have to be public for HelloServer to call them !
	 */
	/**
	 * Load userData from the file, to an arraylist
	 */
	public void loadUsers(String s) throws RemoteException{
		if (s.equals(callerServerIDHash)){
			ArrayList<String> lines = userData.readLines();
			log("[SETUP] - Loading Users : ");
			for (String line : lines){
				try {
					UserData current = Security.stringToUserdata(line);
					userToPass.put(current.getUser(), current.getSalt()+":"+current.getPassword());
					log("[SETUP] | - Loaded : "+current.getUser());
				} catch (NullPointerException npe){
					log("[SETUP] | - line :'"+line+"' Contains no user:salt:password");
				}
			}
		}
	}

	/**
	 * Load chat hustory from the file, to an arraylist
	 */
	public void loadHistory(String s){
		if (s.equals(callerServerIDHash)){
			chatHistory = history.readLines();
			log("[SETUP] - HISTORY LOADED ("+chatHistory.size()+" lines)");
		}
		executorServiceHistory = Executors.newSingleThreadScheduledExecutor();

        executorServiceHistory.scheduleAtFixedRate(() -> {
			if (bufferHistory.size() > 0 ){
				backupHistory(); // backup chat history every 2 minutes
			}
        }, 0, 2, TimeUnit.MINUTES);

	}

	/**
	 * Start logs backup
	 */
	public void startLogs(String identifier)  throws RemoteException{
		if(identifier.equals(callerServerIDHash)){
			log("#### ~New Server Start : "+dtf.format(LocalDateTime.now())+"####");

			executorServiceLogs = Executors.newSingleThreadScheduledExecutor();
	
			executorServiceLogs.scheduleAtFixedRate(() -> {
				if (bufferLog.size() > 0 ){
					backupLog(); // backup server logs every 30 seconds
				}
			}, 0, 30, TimeUnit.SECONDS);
		}
	}

	/**
	 * Log message to the server
	 * @param s message
	 */
	private void log(String s){
		String ss = "("+dtf.format(LocalDateTime.now())+") "+s;
		gui.log(ss);
		bufferLog.add(ss);
	}

	/**
	 * Warns all the clients that the server is closed.
	 */
	public void exitAll() throws RemoteException{
		for (ClientMessagesInterface client : userToClientStub.values()) {
			client.closingServer();
		}
	}


}

