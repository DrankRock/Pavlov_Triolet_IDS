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

    public class Tuple2<A, B> {
        public final A first;
        public final B second;
    
        public Tuple2(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }

    public PhysicalNetwork(int[][] input_array){
        int size = inputGraph.length;
        this.inputGraph = input_array.clone();
        physicalNodes = new ArrayList<>();
        for(int i=0; i<input_array.length; i++){
            // each line is a node
            PhysicalNode pn = new PhysicalNode(i);
            for(int j = 0; j<input_array[i].length; j++){
                int v = input_array[i][j];
                if (v != 0){
                    System.out.print(v);
                    pn.addConnection(j);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
            physicalNodes.add(pn);
        }

        Tuple2<int[], int[][]> dist_pred = new Tuple2<>(new int[size], new int[size][size]);
        dist_pred = Bellman_Ford(0);
        System.out.println("The distance from 0 are : " + dist_pred.first);
        System.out.println("The predecessor of each node are : " + dist_pred.second);
    }

    private Tuple2<int[], int[][]> Bellman_Ford(int from){
        
        // Stack<Integer> visited = new Stack<>();
        // Stack<Integer> notVisited = new Stack<>();
        // for(int i=0; i<size; i++){
        //     if(i == from){
        //         visited.add(from);
        //         continue;
        //     }
        //     notVisited.add(i);
        // }
        int size = inputGraph.length;

        int[] distances = new int[size];
        int[][] predecessors = new int[size][size];
        Arrays.fill(distances, Integer.MAX_VALUE);
        for(int i = 0; i < size; i++){
            predecessors[i] = new int[size];
            Arrays.fill(predecessors[i], -1); //assuming that the label of each node is an natural integer 
        }
        System.out.println(predecessors); 
        distances[from] = 0;

        // Main loop
        // k is very weird, or what I say is : I don't understand its usefulness yet
        for(int k = 1; k < size; k++){
            for(int u = 0; u < size ; u++){
                for(int v = 0; v < size; v++){
                    if(inputGraph[u][v] != 0){
                        if(distances[u] + 1 < distances[v]){
                            distances[v] = distances[u] + 1;
                            // predecessor according to the starting node
                            predecessors[k][v] = u;
                        }
                    }
                }
            }
        }
        
        // return the distances from the starting node and predecessors for each node  
        Tuple2<int[], int[][]> dist_pred = new Tuple2<>(distances, predecessors);
        return dist_pred;
    }

    /*
    public static int Dijkstra(int[][] graph, int a, int b) {
        int n = graph.length;
        int[] distances = new int[n];
        Arrays.fill(distances, INFINITY);
        distances[a] = 0;

        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(i -> distances[i]));
        pq.offer(a);

        while (!pq.isEmpty()) {
            int vertex = pq.poll();
            visited.add(vertex);

            for (int neighbor = 0; neighbor < n; neighbor++) {
                int distance = graph[vertex][neighbor];
                if (distance > 0 && !visited.contains(neighbor)) {
                    int newDistance = distances[vertex] + distance;
                    if (newDistance < distances[neighbor]) {
                        distances[neighbor] = newDistance;
                        pq.offer(neighbor);
                    }
                }
            }
        }

        return distances[b];
    }*/
}
