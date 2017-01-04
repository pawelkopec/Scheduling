package scheduling;

import graph.ListGraph;
import graph.VertexColoring;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

/**
 * Created by Paweł Kopeć on 28.12.16.
 *
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines.
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

    private static final String ILLEGAL_SPEED_NUM = "Algorithm is designed for 3 processing speeds.";
    private static final String ILLEGAL_SPEED_VALUE = "Processing speed must be positive.";

    private ListGraph graph;
    private VertexColoring coloring;
    private double[] speeds;
    private double sumOfSpeeds;
    private int state = NOT_CHECKED;

    public TripleScheduling(ListGraph graph, double[] speeds) {
        setSpeeds(speeds);
        this.graph = graph;
        for(double i : speeds) sumOfSpeeds += i;
        coloring = new VertexColoring(graph);
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
     */
    public VertexColoring findScheduling() {

        if(state == NOT_CHECKED) {
            checkState();
        }
        findOptimalDivision();
        switch(state) {
            case BRUTE_FORCE:
            case BRUTE_FORCE_EASY:
                findBruteForce();
                break;
            case OPTIMAL:
                findOptimal();
                break;
            case SUBOPTIMAL:
                findSuboptimal();
                break;
        }

        return coloring;
    }

    private void findOptimal() {
        if(speeds[2] < speeds[1] + speeds[2]) {
            CLW(findOptimalDivision());
        }
        else {
            int n2 = (int)Math.ceil(0.5 * speeds[1]/(speeds[0] + speeds[1]));
        }
    }

    private void findSuboptimal() {
        //TODO
    }

    private void findBruteForce() {
        //TODO
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
        coloring.setColor(current, currentColor);
        while(!queue.isEmpty()) {
            current = queue.poll();
            currentColor = coloring.getColor(current);
            otherColor = currentColor == 1 ? 2 : 1;

            for(int neighbour : graph.getNeighbours(current)) {
                tempColor = coloring.getColor(neighbour);

                if(tempColor == noColor) {
                    coloring.setColor(neighbour, otherColor);
                    queue.add(neighbour);
                }
                else if(tempColor == currentColor) {
                    return false;
                }
            }
        }

        return true;
    }

    int[] findOptimalDivision() {
        int n = graph.getVertices();
        double[] nFloat = new double[3];
        for(int i = 0; i < speeds.length; i++) {
            nFloat[i] = n * speeds[i] / sumOfSpeeds;
        }

        double time2Floor = Math.floor(nFloat[1]) / speeds[1];
        double time2Ceil= Math.ceil(nFloat[1]) / speeds[1];
        double time3Floor = Math.floor(nFloat[2]) / speeds[2];
        double time3Ceil= Math.ceil(nFloat[2]) / speeds[2];
        double totalTime = n / sumOfSpeeds;


        double maxTime1 = Math.max(time3Floor, Math.max(time2Ceil, totalTime - time3Floor - time2Ceil));
        double maxTime2 = Math.max(time3Ceil, Math.max(time2Floor, totalTime - time3Ceil - time2Floor));
        double maxTime3 = Math.max(time3Ceil, Math.max(time2Ceil, totalTime - time3Ceil - time2Ceil));

        double minTime = Math.min(maxTime1, Math.min(maxTime2, maxTime3));

        int[] division = new int[3];

        if(minTime == maxTime1) {
            division[2] = (int)Math.floor(nFloat[2]);
            division[1] = (int)Math.ceil(nFloat[1]);
        }
        else if(minTime == maxTime2) {
            division[2] = (int)Math.ceil(nFloat[2]);
            division[1] = (int)Math.floor(nFloat[1]);
        }
        else {
            division[2] = (int)Math.ceil(nFloat[2]);
            division[1] = (int)Math.ceil(nFloat[1]);
        }
        division[0] = n - division[1] - division[2];

        assertEquals(division[0] + division[1] + division[2], n);
        System.out.println(Arrays.toString(division));
        System.out.println(Arrays.toString(speeds));

        return division;
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

    private void setSpeeds(double[] speeds) {
        if(speeds.length != 3) {
            throw new IllegalArgumentException(ILLEGAL_SPEED_NUM + ' ' + speeds.length + " given.");
        }
        for(double speed: speeds) {
            if(speed <= 0) {
                throw new IllegalArgumentException(ILLEGAL_SPEED_VALUE);
            }
        }

        Arrays.sort(speeds);
        this.speeds = speeds;
    }
}