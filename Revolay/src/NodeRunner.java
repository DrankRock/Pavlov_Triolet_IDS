import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Matvei Pavlov
 */
public class NodeRunner {
    int value;
    int numberOfElements;
    ArrayList<Integer> connectedTo;
    private PhysicalNode pn;
    public NodeRunner(String[] args){
        value = Integer.parseInt(args[0]);
        numberOfElements = Integer.parseInt(args[1]);
        connectedTo = new ArrayList<>();
        for(int i=2; i< args.length; i++){
            connectedTo.add(Integer.parseInt(args[i]));
        }
    }
    public void init(){
        pn = new PhysicalNode(value, connectedTo, numberOfElements);
        NodeGUI gui = new NodeGUI(Color.GRAY, value, numberOfElements, this);
    }

    public void ping(int dest){
        try {
            System.out.println("Bug !");
            pn.sendMessage(new Message(value, dest, "Ping"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){

    }
}
