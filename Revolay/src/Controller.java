import java.awt.*;

/**
 * Controller class to make the communication between a GUI and a Client (consumer or producer)
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public class Controller {
    private NumberDisplay gui;
    private Model mdl;
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

    public void init(Model model){
        gui = new NumberDisplay(
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
        System.out.println("Action handled, sending ping to " + next);
        mdl.ping(number, next);
    }

    public void sendLeft(){
        mdl.ping(number, prec);
    }

    public void ping(){
        gui.flicker();
    }
}
