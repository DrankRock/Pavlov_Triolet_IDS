import java.awt.*;
import java.util.Arrays;

/**
 * Utility methods working just fine as static methods
 * @author Matvei Pavlov
 */
public final class Utils {
    /**
     * Get the luminance of a color
     * @param c the color
     * @return the luminance as a double
     */
    static public double getLuminance(Color c){
        //  (0.2126*R + 0.7152*G + 0.0722*B)
        return 0.2125*c.getRed() + 0.7152*c.getGreen() + 0.0722*c.getBlue();

    }

    /**
     * Determines objectively if a color is bright rather than dark
     * @param c the color
     * @return true if the color is bright
     */
    static public boolean isBright(Color c){
        return getLuminance(c) > 127.5;
    }

    /**
     * Bellman Ford Algoritm's implementation to find the shortest path to all the nodes of a graphe
     * @param from the starting node
     * @param inputGraph the graphe as an array
     * @return the predecessors of each node
     */
    static public int[] Bellman_Ford(int from, int[][] inputGraph){

        int size = inputGraph.length;
        int[] distances = new int[size];
        int[] predecessors=  new int[size];

        Arrays.fill(distances, Integer.MAX_VALUE - 5);
        Arrays.fill(predecessors, -1); //assuming that the label of each node is an natural integer
        distances[from] = 0;

        // Main loop
        for(int k = 1; k < size; k++){
            for(int u = 0; u < size ; u++){
                for(int v = 0; v < size; v++){
                    if(inputGraph[u][v] != 0){
                        if(distances[u] + 1 < distances[v]){
                            distances[v] = distances[u] + 1;
                            // predecessor according to the starting node
                            predecessors[v] = u;
                        }
                    }
                }
            }
        }

        return predecessors;
    }

    /**
     * Convert a predecessor array to a directions array, to have, for each node, the direction the from
     * node has to ping to access this node
     * @param predecessors the list of predecessors from the bellman ford
     * @param from the starting node
     * @return the directions array
     */
    public static int[] predToDirection(int[] predecessors, int from){
        int[] directions = predecessors.clone();
        for(int i=0; i< predecessors.length; i++){
            // don't check direction of node to himself
            if (i != from ) {
                int current = i;
                int previous = predecessors[current];
                while (previous != from){
                    current = previous ;
                    previous = predecessors[current];
                }
                directions[i] = current;
            }
        }
        return directions;
    }
}
