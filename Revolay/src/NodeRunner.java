import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Matvei Pavlov
 *
 * Run a node by creating its physical version, and processing the sending of messages
 */
public class NodeRunner {
    int value;
    int numberOfElements;
    ArrayList<Integer> connectedTo;
    private PhysicalNode pn;
    private int[] directions;
    int[][] graph;
    PhysicalNodeGUI gui;
    SingleNode caller;

    /**
     * Constructor from command line arguments. Initialize the node's directions from the given graph file
     * @param args command line arguments
     */
    public NodeRunner(String[] args){
        if (args.length != 3){
            System.out.println("Usage : java Main.java <graphe file> <connections of node> <node's number>");
            System.exit(1);
        }
        this.graph = readArray(args[0]);
        connectedTo = new ArrayList<>();
        String[] bareConnected = args[1].replaceAll("\\[|'|]|\\s", "").split(",");
        for (String s : bareConnected){
            connectedTo.add(Integer.valueOf(s));
        }
        this.value = Integer.parseInt(args[2]);
        int[] preds = Utils.Bellman_Ford(value, this.graph);
        this.directions = Utils.predToDirection(preds, value);
        this.numberOfElements = preds.length;
    }

    /**
     * Read a graph array from a file and store it in an int[][] array
     * @param fileName
     * @return
     */
    private int[][] readArray(String fileName){
        ArrayList<String> file = new ArrayList<>();
        // Convert the file to an arraylist of strings
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches("\\d.*")) { // check if the line starts with a digit
                    file.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while opening the file "+fileName);
            e.printStackTrace();
            System.exit(1);
        }
        // convert the arraylist of strings to a 2D array of int
        int rows = file.size();
        int[][] array = new int[rows][];
        int i = 0;
        for (String s : file){
            String[] current = s.split(" ");
            array[i] = new int[current.length];
            int j = 0;
            for ( String var : current){
                int currentT = Integer.valueOf(var);
                array[i][j] = currentT;
                j++;
            }
            i++;
        }
        return array;
    }

    /**
     * Print an array. Used for debugging purposes
     * @param array the array to print
     */
    @SuppressWarnings("unused")
    private void printArray(int[][] array){

        for (int i=0; i< array.length; i++){
            StringBuilder text = new StringBuilder();
            for (int j=0; j<array[i].length; j++){
                text.append(array[i][j]).append(" ");
            }
            System.out.println(text);
        }
    }

    /**
     * Initialize the NodeRunner by creating its physical node,adding its directions, and launching the gui.
     */
    public void init(SingleNode caller){
        this.caller = caller;
        pn = new PhysicalNode(value, connectedTo, numberOfElements, this);
        for (int i=0; i<directions.length; i++) {
            if (i != value){
                pn.addDirection(i, this.directions[i]);
            }
        }
        this.gui = new PhysicalNodeGUI(Color.GRAY, value, numberOfElements, this);
    }

    public void received(Message msg){
        this.caller.receivedMessage(msg);
    }

    /**
     * Send a ping to the Physical Node of id dest
     * @param dest the id of the destination
     */
    public void ping(int dest){
        try {
            pn.sendMessage(new Message(value, dest, "Ping"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getValue(){
        return this.value;
    }
    public int getTotalNodes(){
        return this.numberOfElements;
    }
}
