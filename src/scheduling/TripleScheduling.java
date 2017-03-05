package scheduling;

import graph.RegularListGraph;
import graph.VertexColoring;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Paweł Kopeć on 28.12.16.
 * <p>
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines.
 */
public class TripleScheduling {

    public static final String GRAPH_NOT_CUBIC = "Algorithm is applied only to cubic graphs.";

    /**
     * Possible states of incompatibility graph.
     * <p>
     * OPTIMAL and SUBOPTIMAL states means that class can
     * give optimal and suboptimal solutions in polynomial time.
     * <p>
     * BRUTE_FORCE state means that solution will be optimal.
     * but will require super-polynomial time.
     * <p>
     * BRUTE_FORCE_EASY state means the same as above, but graph
     * is so small, that complexity is acceptable.
     */
    public static final int OPTIMAL = 1;
    public static final int SUBOPTIMAL = 2;
    public static final int BRUTE_FORCE = 3;
    public static final int BRUTE_FORCE_EASY = 4;
    public static final int NOT_CHECKED = 5;

    private RegularListGraph graph;
    private VertexColoring coloring;
    private double[] speeds;
    private int state = NOT_CHECKED;

    public TripleScheduling(RegularListGraph graph, double[] speeds) {
        if (graph.getDegree() != 3 || !graph.isRegular()) {
            throw new IllegalArgumentException(GRAPH_NOT_CUBIC);
        }

        this.graph = graph;
        this.speeds = speeds;
        coloring = new VertexColoring(graph);
    }

    /**
     * Check which algorithm will will be needed
     * for that particular graph.
     * <p>
     * Warning: Checking if graph is 2-chromatic
     * requires O(|V|) time.
     */
    private void checkState() {
        if (graph.getVertices() < 8) {
            state = BRUTE_FORCE_EASY;
        } else if (is2chromatic()) {
            state = OPTIMAL;
        } else if (speeds[2] > speeds[1] && speeds[1] == speeds[0]) {
            state = SUBOPTIMAL;
        } else {
            state = BRUTE_FORCE;
        }
    }

    /**
     * Apply different scheduling algorithms
     * depending on the which graph case is considered.
     */
    public VertexColoring findScheduling() {

        if (state == NOT_CHECKED) {
            checkState();
        }

        switch (state) {
            case BRUTE_FORCE:
            case BRUTE_FORCE_EASY:
                findBruteForce();
                break;
            case OPTIMAL:
                return new BicubicScheduling(graph, coloring, speeds).findColoring();
            case SUBOPTIMAL:
                return new TricubicScheduling(graph, coloring, speeds).findColoring();
        }

        return coloring;
    }

    /**
     * Check if graph is 2-chromatic via BFS.
     * Assign colors of numbers 1 and 2 if it is true.
     *
     * @return true if graph is 2-chromatic
     */
    private boolean is2chromatic() {
        Queue<Integer> queue = new LinkedList<>();
        int current = 0, currentColor = 1, otherColor, noColor = 0, tempColor;

        queue.add(current);
        coloring.set(current, currentColor);
        while (!queue.isEmpty()) {
            current = queue.poll();
            currentColor = coloring.get(current);
            otherColor = currentColor == 1 ? 2 : 1;

            for (int neighbour : graph.getNeighbours(current)) {
                tempColor = coloring.get(neighbour);

                if (tempColor == noColor) {
                    coloring.set(neighbour, otherColor);
                    queue.add(neighbour);
                } else if (tempColor == currentColor) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Find optimal solution in
     * non-polynomial time.
     */
    private void findBruteForce() {
        //TODO
    }

    public int getState() {
        return state;
    }
}