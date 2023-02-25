import java.rmi.*;
import java.util.ArrayList;

/**
 * Class used to receive messages from server and send them to the GUI
 */
public class ClientMessages implements ClientMessagesInterface {
    ChatApp gui;
    String username;
    public ClientMessages(String user){
        this.username = user;
    }

    public void run(Info_itf_Impl inf) throws RemoteException, NotBoundException{
        gui = new ChatApp(inf);
        gui.run();
    }

    public void displayMessage(String s) throws RemoteException{
        gui.addMessage(s);
    }

    public void displayMessage(String user, String s) throws RemoteException{
        gui.addMessage(user, s);
    }

    public void displayMessage(ArrayList<String> ss) throws RemoteException{
        gui.addMessages(ss);
    }

    public void closingServer() throws RemoteException{
        gui.forceExit();
    }
}