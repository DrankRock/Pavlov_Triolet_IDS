import java.io.IOException;
import java.util.ArrayList;

/**
 * Supposed to act as a Controller, that launches the physical node, and the virtual node, and reacts
 * to an action on the virtual node, and process this to the physical node and vice versa
 * @author Matvei Pavlov
 */
public class SingleNode {
    /**
     * The value of the node
     */
    private int currentWindow;
    /**
     * The total number of nodes
     */
    private int totalWindows;
    /**
     * The wrapper/controller of the virtual node
     */
    private Controller ctrl;
    /**
     * The wrapper/controller/model of the physical node
     */
    private NodeRunner physicalNode;

    /**
     * Constructor of the SingleNode
     * @param args the command line arguments of the Main
     */
    public SingleNode(String[] args){
        physicalNode = new NodeRunner(args); // physical node launching
        this.currentWindow = physicalNode.getValue();
        this.totalWindows = physicalNode.getTotalNodes();
        ctrl = new Controller(currentWindow, totalWindows); // virtual node launching
    }

    /**
     * Initializes the content of the physicical and virtual nodes.
     * The execution of this class is mandatory for a correct running of the software.
     */
    public void init(){
        ctrl.init(this);
        physicalNode.init(this);
    }

    /**
     * Called when the Physical node received a message that was meant to himself.
     * @param msg the message
     */
    public void receivedMessage(Message msg){
        System.out.println("Node "+currentWindow+" received message : "+msg.getMessage());
        ctrl.ping();
    }

    /**
     * Called when the Virtual node gui asks to send a ping to a destination
     * @param dest the value of the destination node
     */
    public void sendMessage(int dest){
        this.physicalNode.ping(dest);
    }

}
