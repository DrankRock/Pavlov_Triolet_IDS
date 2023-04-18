import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * @author Matvei Pavlov
 * @author Hugo Triolet
 * Physical representation of a network, using PhysicalNode(s)
 */
public class PhysicalNetwork {

    private ArrayList<PhysicalNode> physicalNodes;
    private int[][] inputGraph;
    public int[] distances;
    public int[] predecessors;

    public PhysicalNetwork(int[][] input_array, int size){
        this.inputGraph = new int[size][size];
        this.inputGraph = input_array.clone();
        physicalNodes = new ArrayList<>();
        this.distances = new int[size];
        this.predecessors=  new int[size];
        /*for(int i=0; i<input_array.length; i++){
            // each line is a node
            PhysicalNode pn = new PhysicalNode(i);
            for(int j = 0; j<input_array[i].length; j++){
                int v = input_array[i][j];
                if (v != 0){
                    System.out.print(v);
                    pn.addConnection(j);
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
            physicalNodes.add(pn);
        }*/
    }


    public void Bellman_Ford(int from){
        
        // Stack<Integer> visited = new Stack<>();
        // Stack<Integer> notVisited = new Stack<>();
        // for(int i=0; i<size; i++){
        //     if(i == from){
        //         visited.add(from);
        //         continue;
        //     }
        //     notVisited.add(i);
        // }
        int size = this.inputGraph.length;
        System.out.println("\nSize of the graph (number of vertices) : " + size + "\n");
        Arrays.fill(distances, Integer.MAX_VALUE - 5);
        Arrays.fill(predecessors, -1); //assuming that the label of each node is an natural integer 
        System.out.println("=================================");
        System.out.println("INIT distances : " + Arrays.toString(distances));
        System.out.println("INIT predecessors : " + Arrays.toString(predecessors));
        System.out.println("=================================\n");
        distances[from] = 0;

        // Main loop
        for(int k = 1; k < size; k++){
            for(int u = 0; u < size ; u++){
                for(int v = 0; v < size; v++){
                    if(this.inputGraph[u][v] != 0){
                        if(distances[u] + 1 < distances[v]){
                            distances[v] = distances[u] + 1;
                            // predecessor according to the starting node
                            predecessors[v] = u;
                        }
                    }
                }
            }
        }
        return;
    }

    public static void main(String[] args){
        int[][] arr = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 1, 1},
            {1, 1, 0, 0, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 0, 0, 0}
        };
        for(int i = 0; i< 5; i++){
            System.out.print("{");
            for(int j =0; j< 4; j++){
                System.out.print(arr[i][j]+",");
            }
            System.out.print(arr[i][4]);
            System.out.print("}\n");
        }
        PhysicalNetwork ph = new PhysicalNetwork(arr, 5);
        ph.Bellman_Ford(0);
        System.out.println("\nThe distance from 0 are : " + Arrays.toString(ph.distances));
        System.out.println("The predecessor of each node are : " + Arrays.toString(ph.predecessors) + "\n");
    }
}
