package com.vangle;

import java.util.*;

/**
 * Created by rparra on 20/2/18.
 *
 *
 */
public class Solution {


    /**
     * For the weighted graph:
     * 1. The number of nodes is <name>_nodes.
     * 2. The number of edges is <name>_edges.
     * 3. An edge exists between <name>_from[i] to <name>_to[i] and the weight of the edge
     * is <name>_weight[i].
     **/
    static int minCost(int g_nodes, int[] g_from, int[] g_to, int[] g_weight) {
        List<Map<Integer, Integer>> graph = new ArrayList<>();
        for (int i = 0; i < g_nodes; i++) {
            graph.add(new HashMap<>());
        }

        /* We build our graph representation, changing to 0-based indexing for convenience */
        for (int i = 0; i < g_from.length; i++) {
            graph.get(g_from[i] - 1).put(g_to[i] - 1, g_weight[i]);
        }
        return dijkstra(graph, g_nodes - 1);
    }

    /**
     * A slightly modified version of Dijkstra's shortest path algorithm considering a default weight of 1 for nodes
     * that are not connected by an edge.
     *
     * @param graph the graph represented as a list of maps, where
     *              the ith map contains tuples <k, w> with k being the destination node
     *              of the edge and w the weight of the edge
     *              Using a map instead of a list ensures O(1) checking of and edge existence.
     * @return the distance of the shortest path from 0 to to, or Integer.MAX_value if no path exists
     *
     * The asymptotic runtime complexity of this solution is O(E log V) because:
     *
     *  - The initialization of the data structures is O(V log V).
     *  - We call pollFirst() on our priority queue V times, each call is O(log V), hence this costs O(V log V)
     *  - We update vertex distances at most E times, each updates is O(log V), hence this costs O(E log V)
     *
     *  The cost of the algorithm is O(V log V + V log V + E log V), since E dominates V according to the
     *  problem constraints, we arrive at O(E log V).
     */
    static int dijkstra(List<Map<Integer, Integer>> graph, int to) {

        int vertexCount = to + 1;
        Boolean[] inTree = new Boolean[vertexCount]; // flag to indicate if a vertex has been added to the minimum spanning tree (MST)
        Integer[] distance = new Integer[vertexCount]; // keeps track of the minimum known distance from node 1 to all other nodes
        final int NO_EDGE_WEIGHT = -1;


        /*
         * Using a tree set ensures log(N) for add and remove
         * docs.oracle.com/javase/7/docs/api/java/util/TreeSet.html
         */
        TreeSet<Integer> q = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                if (distance[a] > distance[b]) {
                    return 1;
                } else if (distance[a] < distance[b]) {
                    return -1;
                } else {
                    return a.compareTo(b);
                }
            }
        });


        /* First we initialize our data structures:
            - No node is in the MST yet.
            - Distance from vertex 1 to every vertex is infinity. */
        for (int i = 0; i < vertexCount; i++) {
            inTree[i] = false;
            distance[i] = Integer.MAX_VALUE;
            if (i > 0) q.add(i);
        }


        int currentVertex = 0;
        distance[currentVertex] = 0; // distance from vertex 1 to itself is 0

        // while the MST is incomplete
        while (!q.isEmpty()) {
            // First, we update the known distances for all neighbors of the current vertex
            for (int i = 0; i < vertexCount; i++) {
                if (i != currentVertex) {

                    // We check whether an edge exists in the opposite direction, if not we add a 1-edge
                    int defaultWeight = (graph.get(i).get(currentVertex) == null) ? 1 : NO_EDGE_WEIGHT;
                    int weight = graph.get(currentVertex).getOrDefault(i, defaultWeight);

                    if (distance[i] > distance[currentVertex] + weight && weight != NO_EDGE_WEIGHT) {
                        q.remove(i);
                        distance[i] = distance[currentVertex] + weight;
                        q.add(i);
                    }
                }
            }

            /*
            * Second, we select the vertex with the minimum distance to vertex 1 * that is not yet in the MST
            */
            currentVertex = q.pollFirst();
        }
        return distance[to];
    }
}
