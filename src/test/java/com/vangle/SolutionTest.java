package com.vangle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by rparra on 21/2/18.
 */
public class SolutionTest {

    /**
     * A directed edge already exists from node 1 to node 2 and the path 1 → 2 is the minimum cost path,
     * so the function returns 3.
     */
    @Test
    public void singleExistingEdge() {
        int g_nodes = 2;
        int[] g_from = {1};
        int[] g_to = {2};
        int[] g_weight = {3};
        assertEquals(3, Solution.minCost(g_nodes, g_from, g_to, g_weight));
    }

    /**
     * As graph g has no edge between node 1 and node 3, we can add an extra edge from node1 to node 3 having weight 1.
     * Thus, the path 1 → 3 is the minimum weight path and the function returns 1.
     */
    @Test
    public void noEdgeBetweenSourceAndDestination() {
        int g_nodes = 3;
        int[] g_from = {1};
        int[] g_to = {2};
        int[] g_weight = {3};
        assertEquals(1, Solution.minCost(g_nodes, g_from, g_to, g_weight));
    }

    /**
     * A directed edge already exists from node 1 to node 4 and the path 1 → 4 is the minimum cost path,
     * so the function returns 3.
     */
    @Test
    public void multipleEdges() {
        int g_nodes = 4;
        int[] g_from = {1, 1, 1, 2};
        int[] g_to = {2, 3, 4, 1};
        int[] g_weight = {3, 3, 3, 3};
        assertEquals(3, Solution.minCost(g_nodes, g_from, g_to, g_weight));
    }

    /**
     *  A directed edge 2->1 exists, so no 1-edge 1->2 can be inserted. Hence, cost should be Integer.MAX_VALUE.
     */
    @Test
    public void edgeInOppositeDirection() {
        int g_nodes = 2;
        int[] g_from = {2};
        int[] g_to = {1};
        int[] g_weight = {3};
        assertEquals(Integer.MAX_VALUE, Solution.minCost(g_nodes, g_from, g_to, g_weight));
    }


    /**
     * For this test we are actually assuming a scenario beyond the problem constraints.
     * The largest path of an n-vertex graph is given by G = 1->2->3->...->n, i.e a straight line passing through
     * all vertices from 1 to n. This graph requires g_nodes - 1 edges.
     * <p>
     * However, given the additional condition that 1-edges can be added for every pair of non-connected vertices,
     * we must ensure that 'shortcuts' don't occur. To guarantee this, we add back-edges; for vertex i we add
     * back-edges i-2->i, i-3->i, i-4->i ... 1->i. After this, we end up requiring [g_nodes * (g_nodes - 1) / 2] edges
     * which is present at the problem statement.
     * <p>
     * The lenght of the longest path would then be (g_nodes - 1) * 10^6
     * <p>
     * Let's refresh our memory:
     * <p>
     * 1 <= g_edges <= Math.min(10 ^ 4, g_nodes * (g_nodes - 1) / 2)
     * <p>
     * After a certain number of nodes, the right-hand-side expression is dominated by 10 ^ 4. Hence we can't construct
     * the graph described above for g_nodes = 10 ^ 3. For the purpose of making this test more interesting, we remove
     * the 10 ^ 4 upper bound.
     * <p>
     * Even in this scenario, it holds that (10^3 - 1) * 10^6 (~10^9) < 2^31 (Integer.MAX_VALUE),
     * hence an int is enough to store the result. See https://www.wolframalpha.com/input/?i=10%5E9+<+2%5E31
     */
    @Test
    public void longestPath() {
        int g_nodes = (int) Math.pow(10, 3);
        int g_edges = g_nodes * (g_nodes - 1) / 2;
        //int g_edges = Math.min(10 ^ 4, g_nodes * (g_nodes - 1) / 2);
        int g_max_weight = (int) Math.pow(10, 6);

        List<Integer> gFrom = new ArrayList<>();
        List<Integer> gTo = new ArrayList<>();
        List<Integer> gWeight = new ArrayList<>();


        // First we add edges corresponding to the straight line
        for (int i = 1; i < g_nodes; i++) {
            gFrom.add(i);
            gTo.add(i + 1);
            gWeight.add(g_max_weight);
        }


        // Then we add back-edges to avoid 1-edge insertion
        for (int i = 3; i <= g_nodes; i++) {
            for (int j = 1; j < i - 1; j++) {
                gFrom.add(i);
                gTo.add(j);
                gWeight.add(g_max_weight);
            }
        }

        // We are using the maximum possible number of edges
        assertEquals(g_edges, gFrom.size());

        int[] g_from = gFrom.stream().mapToInt(i -> i).toArray();
        int[] g_to = gTo.stream().mapToInt(i -> i).toArray();
        int[] g_weight = gWeight.stream().mapToInt(i -> i).toArray();

        assertEquals(g_max_weight * (g_nodes - 1), Solution.minCost(g_nodes, g_from, g_to, g_weight));

    }

    /**
     * We add back-edges to create loops, to make our algorithm does not follow them.
     * We also add back-edges to ensure no unwanted 1-edges are inserted.
     */
    @Test
    public void loopEdges() {
        int g_nodes = 4;
        int[] g_from = {1, 2, 3, 3, 4, 3, 4};
        int[] g_to = {2, 3, 4, 1, 1, 1, 2};
        int[] g_weight = {3, 3, 3, 3, 3, 3, 3};
        assertEquals(9, Solution.minCost(g_nodes, g_from, g_to, g_weight));
    }
}
