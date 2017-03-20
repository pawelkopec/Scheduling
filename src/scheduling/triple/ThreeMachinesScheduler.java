package scheduling.triple;

import graph.RegularGraph;
import graph.VertexColoring;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static scheduling.triple.Const.*;

/**
 * Created by Paweł Kopeć on 28.12.16.
 *
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines.
 */
public class ThreeMachinesScheduler {

    public static final String ILLEGAL_SPEED_VALUE = "Processing speed must be positive.";
    public static final String ILLEGAL_SPEED_NUM = "Algorithm is designed for 3 processing speeds.";
    public static final String GRAPH_NOT_CUBIC = "Algorithm is applied only to cubic graphs.";

    /**
     * Possible states of incompatibility graph.
     *
     * OPTIMAL and SUBOPTIMAL states means that class can
     * give optimal and suboptimal solutions in polynomial time.
     *
     * BRUTE_FORCE state means that solution will be optimal.
     * but will require super-polynomial time.
     *
     * BRUTE_FORCE_EASY state means the same as above, but graph
     * is so small, that complexity is acceptable.
     */
    public static final int OPTIMAL = 1;
    public static final int SUBOPTIMAL = 2;
    public static final int BRUTE_FORCE = 3;
    public static final int BRUTE_FORCE_EASY = 4;
    public static final int NOT_CHECKED = 5;

    private RegularGraph graph;
    private VertexColoring coloring;
    private double[] speeds;
    private int state = NOT_CHECKED;

    public ThreeMachinesScheduler(RegularGraph graph, double[] speeds) {
        if (graph.getDegree() != 3 || !graph.isRegular()) {
            throw new IllegalArgumentException(GRAPH_NOT_CUBIC);
        }

        this.graph = graph;
        setSpeeds(speeds);
        coloring = new VertexColoring(graph);
    }

    /**
     * Check which algorithm will be needed
     * for that particular graph.
     *
     * Warning: Checking if graph is 2-chromatic
     * requires O(|V|) time.
     */
    private void checkState() {
        if (graph.getVertices() < 8) {
            state = BRUTE_FORCE_EASY;
        } else if (is2chromatic()) {
            state = OPTIMAL;
        } else if (speeds[FASTEST] > speeds[MIDDLE] && speeds[MIDDLE] == speeds[SLOWEST]) {
            state = SUBOPTIMAL;
        } else {
            state = BRUTE_FORCE;
        }
    }

    /**
     * Apply different scheduling algorithms
     * depending on which graph case is considered.
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
        int current = 0, currentColor = A, otherColor, tempColor;

        queue.add(current);
        coloring.set(current, currentColor);
        while (!queue.isEmpty()) {
            current = queue.poll();
            currentColor = coloring.get(current);
            otherColor = currentColor == A ? B : A;

            for (int neighbour : graph.getNeighbours(current)) {
                tempColor = coloring.get(neighbour);

                if (tempColor == NO_COLOR) {
                    coloring.set(neighbour, otherColor);
                    queue.add(neighbour);
                }
                else if (tempColor == currentColor) {
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

    private void setSpeeds(double[] speeds) {
        if (speeds.length != 3) {
            throw new IllegalArgumentException(ILLEGAL_SPEED_NUM + ' ' + speeds.length + " given.");
        }
        for (double speed : speeds) {
            if (speed <= 0) {
                throw new IllegalArgumentException(ILLEGAL_SPEED_VALUE);
            }
        }

        Arrays.sort(speeds);
        this.speeds = speeds;
    }

    public int getState() {
        if (state == NOT_CHECKED) {
            checkState();
        }

        return state;
    }
}