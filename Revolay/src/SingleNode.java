import java.io.IOException;
import java.util.ArrayList;

/**
 * Supposed to act as a model, that launches the physical node, and the virtual node, and reacts
 * to an action on the virtual node, and process this to the physical node and vice versa
 * @author Matvei Pavlov
 */
public class SingleNode {
    private int currentWindow;
    private int totalWindows;
    private Controller ctrl;
    private NodeRunner physicalNode;

    public SingleNode(String[] args){
        physicalNode = new NodeRunner(args, this); // physical node launching
        physicalNode.init();
        this.currentWindow = physicalNode.getValue();
        this.totalWindows = physicalNode.getTotalNodes();
        ctrl = new Controller(currentWindow, totalWindows); // virtual node launching
    }

    public void init(){
        ctrl.init(this);
    }

    public void receivedMessage(Message msg){
        System.out.println("Node "+currentWindow+" received message : "+msg.getMessage());
        ctrl.ping();
    }

    public void sendMessage(int dest){
        this.physicalNode.ping(dest);
    }

}
