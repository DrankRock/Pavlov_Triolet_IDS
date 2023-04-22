/**
 * Main class used as a Wrapper for SingleNode, for the clarity of the execution
 * @author Matvei Pavlov
 */
public class Main {
    /**
     * Constructor of the main, by default, is called with the command line arguments
     * @param args the command line arguments
     */
    public static void main(String[] args ){
        SingleNode singleNode = new SingleNode(args);
        singleNode.init();
    }

    /**
     * Empty Constructor of the main, to satisfy javadoc's infinite thirst for docs.
     */
    public Main(){
        super();
    }
}
