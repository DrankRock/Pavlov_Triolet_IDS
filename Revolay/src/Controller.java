import java.awt.*;

/**
 * Controller class to make the communication between a GUI and a Client (consumer or producer)
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public class Controller {
    Client client;
    Gui gui;

    /**
     * Constructor of a Controller
     * @param c the Client (consumer or producer)
     * @param mode The mode (consumer or producer)
     */
    public Controller(Client c, String mode){
        client = c;
        gui = new Gui(this, mode);
    }

    /**
     * If producer GUI clicks on Ping, client is a producer and must send a ping
     * @param c the color to be sent with the ping
     */
    public void handlePress(Color c){
        client.sendPing(c);
    }

    /**
     * Set the color of the background, called by the consumer in case of pinf received
     * @param c
     */
    public void setColor(Color c){
        gui.changeColor(c);
    }

    /**
     * Called when Producer receives a Pong
     */
    public void pong(){
        gui.pong();
    }
}
