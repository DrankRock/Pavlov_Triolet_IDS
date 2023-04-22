import java.awt.*;

/**
 * Controller class to make the communication between a GUI and a Client (consumer or producer)
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public class Controller {
    private View gui;
    private SingleNode mdl;
    private int number;
    private int next;
    private int prec;
    private int totalWindows;
    public Controller(int number, int totalWindows){
        this.number = number;
        this.totalWindows = totalWindows;
        prec = (number-1+totalWindows)%totalWindows;
        next = (number+1)%totalWindows;
    }

    public void init(SingleNode model){
        gui = new View(
                number, new Color(
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255)
                ),
                totalWindows,
                this
        );
        mdl = model;
    }


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
