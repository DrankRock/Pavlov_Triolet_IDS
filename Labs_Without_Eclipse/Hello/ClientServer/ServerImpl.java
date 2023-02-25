import java.nio.charset.Charset;
import java.rmi.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * NOTE :  ID is actually useless I think, because everything is done through a unique username
 * I make it work, and then i'll delete if necessary
 */
public  class ServerImpl implements Server {

	private HashMap<String, String> userToPass;
	private HashMap<String, String> userToID;
	private HashMap<String, ClientMessagesInterface> userToClientStub;
	private ArrayList<String> chatHistory;
	private ArrayList<String> bufferHistory;
	private ArrayList<String> bufferLog;
	private ArrayList<String> activeUsers;
	public SecureRandom r = new SecureRandom();
	FileLoader history;
	FileLoader userData;
	FileLoader logFile;

	ServerApp gui;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss:SSS:AAAA");

	private String callerServerIDHash;

	ScheduledExecutorService executorServiceHistory;
	ScheduledExecutorService executorServiceLogs;
 
	public ServerImpl(String s, int i){
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

	public void startGUI(String s) throws RemoteException, NotBoundException{
		if (s.equals(callerServerIDHash)){
			gui = new ServerApp();
			gui.run();
			gui.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						System.out.println("Exit all users gui");
						exitAll();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

	private synchronized void backupHistory(){
		ArrayList<String> histo = new ArrayList<>(bufferHistory);
		bufferHistory.clear();
		for (String line : histo){
			history.addLine(line);
		}
		log("[INFO] - BACK UP HISTORY DONE, "+histo.size()+" entries added.");
	}

	private synchronized void backupLog(){
		ArrayList<String> logs = new ArrayList<>(bufferLog);
		bufferLog.clear();
		for (String line : logs){
			logFile.addLine(line);
		}
		log("[INFO] - BACK UP LOGFILE DONE, "+logs.size()+" entries added.");
	}


	public String connect(String user, String pass, ClientMessagesInterface cmi) throws RemoteException{
		String ret = null;
		log("[CONNECT] ("+user+") - "+user);

		if (userToPass.containsKey(user)){
			log("[CONNECT] ("+user+") - user exists");
		
			if (arePasswordsEqual(userToPass.get(user), pass)){
				log("[CONNECT] ("+user+") - "+user+" successfully connected.");
				byte[] array = new byte[7];
				new SecureRandom().nextBytes(array);
				ret = new String(array, Charset.forName("UTF-8"));
				//cmi.displayMessage("Welcome back, "+user+".");
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

			byte[] array = new byte[7]; // length is bounded by 7
			new SecureRandom().nextBytes(array);
			ret = new String(array, Charset.forName("UTF-8"));
		}
		if (ret != null){
			userToID.put(user, ret);
			userToClientStub.put(user, cmi);
		}
		return ret;
	}

	public void notifyOfActiveGUI(Info_itf_Impl inf) throws RemoteException{
		if (inf.getID().equals(userToID.get(inf.getName()))){
			// if id corresponds to the one given to the user
			activeUsers.add(inf.getName());
			giveHistoryToUser(inf.getName());
		}
	}

	private void giveHistoryToUser(String user) throws RemoteException{

		userToClientStub.get(user).displayMessage(this.chatHistory);
	}

	public boolean arePasswordsEqual(String saltAndPass, String passwd){
		String[] spl = saltAndPass.split(":");
		String hashed = Security.encode(passwd, spl[0]);
		return spl[1].equals(hashed);
	}

	public void message(Info_itf_Impl infos, String s) throws RemoteException{
		// broadcast message
		
		log("[INFO] - Message received from <"+infos.getName()+">\n    Added to history !");
		chatHistory.add(infos.getName()+": "+s);
		bufferHistory.add(infos.getName()+": "+s);
		for (ClientMessagesInterface client : userToClientStub.values()) {
			client.displayMessage(infos.getName(), s);
		}
	}

	public void disconnect(Info_itf_Impl infos) throws RemoteException{
		log("[DISCONNECT] - Removing "+infos.getName()+" from active users");
		userToClientStub.remove(infos.getName());
		activeUsers.remove(infos.getName());
	}

	/*
	 *  LOADERS have to be public for HelloServer to call them !
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

	public void loadHistory(String s){
		if (s.equals(callerServerIDHash)){
			chatHistory = history.readLines();
			log("[SETUP] - HISTORY LOADED ("+chatHistory.size()+" lines)");
		}
		executorServiceHistory = Executors.newSingleThreadScheduledExecutor();

        executorServiceHistory.scheduleAtFixedRate(() -> {
			if (bufferHistory.size() > 0 ){
				backupHistory();
			}
        }, 0, 2, TimeUnit.MINUTES);

	}

	public void startLogs()  throws RemoteException{
		log("#### ~New Server Start : "+dtf.format(LocalDateTime.now())+"####");

		executorServiceLogs = Executors.newSingleThreadScheduledExecutor();

        executorServiceLogs.scheduleAtFixedRate(() -> {
			if (bufferLog.size() > 0 ){
				backupLog();
			}
        }, 0, 30, TimeUnit.SECONDS);

	}

	private void log(String s){
		String ss = "("+dtf.format(LocalDateTime.now())+") "+s;
		gui.log(ss);
		bufferLog.add(ss);
	}

	public void exitAll() throws RemoteException{
		for (ClientMessagesInterface client : userToClientStub.values()) {
			client.closingServer();
		}
	}


}

