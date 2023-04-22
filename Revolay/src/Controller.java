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
    /**
     * The gui of the virtual node
     */
    private VirtualNodeGUI gui;
    /**
     * The caller of this controller
     */
    private SingleNode mdl;
    /**
     * The value of the node
     */
    private int number;
    /**
     * The next node's value in nodes number
     */
    private int next;
    /**
     * The precedent node's value in nodes number
     */
    private int prec;
    /**
     * The total number of nodes (thus of windows) that needs to be opened.
     */
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
     * Send a message to the right node as represented, but calls the Model
     */
    public void sendRight(){
        mdl.sendMessage(next);
    }

    /**
     * Send a message to the left node as represented, but calls the Model
     */
    public void sendLeft(){
        mdl.sendMessage(prec);
    }

    /**
     * Make the current window flicker in green color
     */
    public void ping(){
        gui.flicker();
    }
}
