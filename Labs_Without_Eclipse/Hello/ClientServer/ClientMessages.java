import java.rmi.*;

public class ClientMessages implements ClientMessagesInterface {
    ChatApp gui;
    //Info_itf_Impl userInfos;
    public ClientMessages(String name) throws RemoteException, NotBoundException{
        //this.userInfos = infos;
        gui = new ChatApp(name);
        gui.run();
    }

    public void displayMessage(String s) throws RemoteException{
        gui.addMessage(s);
        //System.out.println("[MSG] - "+s);
    }

    public void displayMessage(String user, String s) throws RemoteException{
        gui.addMessage(user, s);
        //System.out.println("[MSG] - "+s);
    }
}