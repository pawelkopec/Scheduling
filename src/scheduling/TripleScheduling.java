package scheduling;

import graph.ListGraph;
import graph.VertexColoring;

import java.util.Arrays;

/**
 * Created by Paweł Kopeć on 28.12.16.
 *
 * Graph subclass designed specifically for scheduling jobs
 * for 3 machines of given speeds.
 *
 * TODO
 * issues with colors array when adding new vertices;
 * think of better data structure (maybe move removeVertex() to another interface?)
 */
public class TripleScheduling {

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

    private static final String ILLEGAL_SPEED_NUM = "Graph is designed for 3 processing speeds.";
    private static final String ILLEGAL_SPEED_VALUE = "Processing speed must be positive.";

    private ListGraph graph;
    private VertexColoring coloring;
    private double[] speeds;
    private int state = NOT_CHECKED;

    public TripleScheduling(ListGraph graph, double[] speeds) {
        checkSpeeds(speeds);
        this.graph = graph;
        this.speeds = speeds;
        this.coloring = new VertexColoring(graph);
    }

    /**
     * Check which algorithm will will be needed
     * for that particular graph.
     *
     * Warning: Checking if graph is 2-chromatic
     *          requires O(|V| + |E|) time.
     */
    private void checkState() {
        if(graph.getVertices() < 8) {
            state = BRUTE_FORCE_EASY;
        }
        else if(is2chromatic()) {
            state = OPTIMAL;
        }
        else if (speeds[2] > speeds[1] && speeds[1] == speeds[0]) {
            state = SUBOPTIMAL;
        }
        else {
            state = BRUTE_FORCE;
        }
    }

    /**
     * Apply different scheduling algorithms
     * depending on the which graph case is considered.
     *
     * @param speeds array with 3 machine processing speeds
     */
    public void findScheduling(double[] speeds) {
        checkSpeeds(speeds);
        Arrays.sort(speeds);

        if(state == NOT_CHECKED) {
            checkState();
        }

        switch(state) {
            case BRUTE_FORCE:
            case BRUTE_FORCE_EASY:
                findBruteForce(speeds);
                break;
            case OPTIMAL:
                findOptimal(speeds);
                break;
            case SUBOPTIMAL:
                findSuboptimal(speeds);
                break;
        }
    }

    private void findOptimal(double[] speeds) {
        //TODO
    }

    private void findSuboptimal(double[] speeds) {
        //TODO
    }

    private void findBruteForce(double[] speeds) {
        //TODO
    }

    private boolean is2chromatic() {
        //TODO
        return false;
    }

    /**
     * Decrease width of colouring using
     * modified CLW procedure.
     *
     * @param sizes array of desired sizes of color classes
     */
    private void decreaseWidth(int[] sizes) {
        //TODO
        CLW(sizes);
    }

    /**
     *
     *
     * @param sizes
     */
    private void CLW(int[] sizes) {
        //TODO
    }

    private void checkSpeeds(double[] speeds) {
        if(speeds.length != 3) {
            throw new IllegalArgumentException(ILLEGAL_SPEED_NUM + speeds.length + " given.");
        }
        for(double speed: speeds) {
            if(speed <= 0) {
                throw new IllegalArgumentException(ILLEGAL_SPEED_VALUE);
            }
        }
    }
}
