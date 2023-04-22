import java.awt.*;

/**
 * Controller class to make the communication between a GUI and a Client (consumer or producer)
 * Due to major modifications in the way everything works together, it is a secondary controller,
 * the main one being SingleNode. This class could be split and merged in SingleNode and VirtualNodeGUI
 * But this modification won't happen because of a serious lack of time.
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public class Controller {
    private VirtualNodeGUI gui;
    private SingleNode mdl;
    private int number;
    private int next;
    private int prec;
    private int totalWindows;

    /**
     * Constructor of the Controller
     * @param number the number of the node
     * @param totalWindows the total number of node that will be opened
     */
    public Controller(int number, int totalWindows){
        this.number = number;
        this.totalWindows = totalWindows;
        prec = (number-1+totalWindows)%totalWindows;
        next = (number+1)%totalWindows;
    }

    /**
     * Initialize the Controller, set the random color of the node and launches the gui
     * @param singleNode the SingleNode who called this class
     */
    public void init(SingleNode singleNode){
        gui = new VirtualNodeGUI(
                number, new Color(
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255)
                ),
                totalWindows,
                this
        );
        mdl = singleNode;
    }

    /**
     * Send a message to the right node in the virtual representation
     */
    public void sendRight(){
        mdl.sendMessage(next);
    }

    public void sendLeft(){
        mdl.sendMessage(prec);
    }

    public void ping(){
        gui.flicker();
    }
}
