import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * @author Matvei Pavlov
 * Physical representation of a network, using PhysicalNode(s)
 */
public class PhysicalNetwork {
    private ArrayList<PhysicalNode> physicalNodes;
    private int[][] inputGraph;
    public PhysicalNetwork(int[][] input_array){
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
                    System.out.print(".");
                }
            }
            System.out.println();
            physicalNodes.add(pn);
        }
        Dijkstra(0);
    }

    private void Dijkstra(int from){
        int size = inputGraph.length;
        Stack<Integer> visited = new Stack<>();
        Stack<Integer> notVisited = new Stack<>();

        int[] distances = new int[size];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[from] = 0;

        for(int i=0; i<size; i++){
            if(i != from){
                notVisited.add(i);
            }
        }
        visited.add(from);

        while (! notVisited.isEmpty()){
            int current = notVisited.pop();


            System.out.println();
        }



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
