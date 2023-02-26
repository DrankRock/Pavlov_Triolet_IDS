import java.rmi.*;
import java.util.ArrayList;

/**
 * Class used to receive messages from server and send them to the GUI
 */
public class ClientMessages implements ClientMessagesInterface {
    ChatApp gui;
    String username;
    /**
     * Constructor
     * @param user username of the caller
     */
    public ClientMessages(String user){
        this.username = user;
    }

    /**
     * run the gui, needs to be outside of the constructor, because the info
     * is incomplete until connection is confirmed
     * @param inf info of the user launching the gui
     * @throws RemoteException RMI Connection issues
	 * @throws NotBoundException RMI Connection issues
     */
    public void run(Info_itf_Impl inf) throws RemoteException, NotBoundException{
        gui = new ChatApp(inf);
        gui.run();
    }

    /**
     * Display message inside gui
     * @param s string to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessage(String s) throws RemoteException{
        gui.addMessage(s);
    }

    /**
     * Display message inside gui, but green
     * @param s string to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessageGreen(String s) throws RemoteException{
        gui.addMessageValid(s);
    }

    /**
     * Display message with user name
     * @param user Username of the sender 
     * @param s string to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessage(String user, String s) throws RemoteException{
        gui.addMessage(user, s);
    }

    /**
     * Display multiple messages (used to load history)
     * @param ss ArraList containing all strings to display
     * @throws RemoteException RMI Connection issues
     */
    public void displayMessage(ArrayList<String> ss) throws RemoteException{
        gui.addMessages(ss);
    }

    /**
     * Called on closing of the server, to warn the user.
     * @throws RemoteException RMI Connection issues
     */
    public void closingServer() throws RemoteException{
        gui.forceExit();
    }
}