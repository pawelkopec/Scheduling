package scheduling;

import graph.Colorable;
import graph.MatrixGraph;
import graph.SubgraphFind;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

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
public class TripleSchedulingGraph extends MatrixGraph implements Colorable, SubgraphFind {

    private static final String ILLEGAL_SPEED_NUM = "Graph is designed for 3 processing speeds.";
    private static final String ILLEGAL_SPEED_VALUE = "Processing speed must be positive.";

    private int[] colors;
    private double speeds[];

    public TripleSchedulingGraph() {
        super();
        speeds = new double[]{1., 1., 1.};
    }

    public TripleSchedulingGraph(double[] speeds) throws IllegalArgumentException {
        super();
        setSpeeds(speeds);
    }

    public TripleSchedulingGraph(int verticesNumber) {
        super(verticesNumber);
        speeds = new double[]{1., 1., 1.};
    }

    public TripleSchedulingGraph(int verticesNumber, double[] speeds) {
        super(verticesNumber);
        setSpeeds(speeds);
    }

    private void setSpeeds(double[] speeds) {
        if(speeds.length != 3) {
            throw new IllegalArgumentException(ILLEGAL_SPEED_NUM + speeds.length + " given.");
        }
        for(double speed: speeds) {
            if(speed <= 0) {
                throw new IllegalArgumentException(ILLEGAL_SPEED_VALUE);
            }
        }
        this.speeds = speeds;
    }

    /**
     * TODO
     * setting speeds when reading from stream
     */
    public TripleSchedulingGraph(InputStream in) {
        super(in);
    }

    @Override
    protected void initContainers(int verticesNumber, int edgesNumber) {
        super.initContainers(verticesNumber, edgesNumber);
        colors = new int[verticesNumber];
    }

    @Override
    public void setColor(int index, int color) throws NoSuchElementException {
        if(!isValidVertex(index)) {
            throw new NoSuchElementException(INVALID_VERTEX);
        }
        colors[index] = color;
    }

    @Override
    public int getColor(int index) throws NoSuchElementException {
        if(!isValidVertex(index)) {
            throw new NoSuchElementException(INVALID_VERTEX);
        }
        return colors[index];
    }

    @Override
    public int[][] getColoredClasses() {
        /**
         * TODO
         * 1. Finish.
         * 2. Maybe store colors as sets of indexes?
         */

        return new int[0][];
    }

    @Override
    public int[] getColored(int color) {
        //TODO
        return new int[0];
    }

    @Override
    public boolean disable(int index) throws NoSuchElementException {
        //TODO
        return false;
    }

    @Override
    public boolean enable(int index) throws NoSuchElementException {
        //TODO
        return false;
    }

    @Override
    public void disableAll() {
        //TODO
    }

    @Override
    public void enableAll() {
        //TODO
    }

    @Override
    public LinkedList<Integer> getAllEnabled() {
        //TODO
        return null;
    }

    @Override
    public LinkedList<Integer> getAllDisabled() {
        //TODO
        return null;
    }

    double[] getSpeeds() {
        return speeds;
    }
}
