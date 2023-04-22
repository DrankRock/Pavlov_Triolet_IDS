import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Matvei Pavlov
 */
public class SingleNode {
    private int totalWindows;
    public SingleNode(int totalWindows){
        this.totalWindows = totalWindows;
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

}
