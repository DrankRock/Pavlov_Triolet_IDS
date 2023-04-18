import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Matvei Pavlov
 */
public class Model {
    private int totalWindows;
    private ArrayList<Controller> controllers;
    public Model(int totalWindows){
        this.totalWindows = totalWindows;
        controllers = new ArrayList<>();
        init();
    }

    public void init(){
        for (int i = 0; i < totalWindows; i++) {
            Controller ctrl = new Controller(i, totalWindows);
            ctrl.init(this);
            controllers.add(ctrl);
        }
    }

    public void ping(int from, int to){
        controllers.get(to).ping();
    }

    public static void main(String[] args) {
        Model model = new Model(20);
        // model.init();
        int[][] arr = {
                {0, 0, 1, 0, 0},
                {0, 0, 1, 1, 1},
                {1, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0}
        };
        PhysicalNetwork pn = new PhysicalNetwork(arr, 5);
        int node0 = 0;
        int node1 = 1;
        int node2 = 2;
        ArrayList<Integer> node0_Connected_to = new ArrayList<>();
        ArrayList<Integer> node1_Connected_to = new ArrayList<>();
        ArrayList<Integer> node2_Connected_to = new ArrayList<>();
        node0_Connected_to.add(1);
        node1_Connected_to.add(0);
        node1_Connected_to.add(2);
        node2_Connected_to.add(1);

        PhysicalNode n0 = new PhysicalNode(node0,node0_Connected_to);
        n0.addDirection(2, 1);
        PhysicalNode n1 = new PhysicalNode(node1,node1_Connected_to);
        PhysicalNode n2 = new PhysicalNode(node2,node2_Connected_to);
        n2.addDirection(0, 1);
        Message myMessage = new Message(0, 2, "Hello from 0 !");
        try {
            n0.sendMessage(myMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
