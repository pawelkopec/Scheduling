package scheduling;

import graph.RegularListGraph;
import graph.VertexColoring;

import java.util.Arrays;

/**
 * Created by Paweł Kopeć on 02.03.17.
 *
 * Base class for finding coloring of
 * cubic incompatibility graphs.
 */
public abstract class CubicScheduling {

    private static final String ILLEGAL_COLORING = "Coloring must have reference to the same graph.";
    private static final String ILLEGAL_SPEED_NUM = "Algorithm is designed for 3 processing speeds.";
    private static final String ILLEGAL_SPEED_VALUE = "Processing speed must be positive.";

    protected RegularListGraph graph;
    protected VertexColoring coloring;
    protected double[] speeds;
    protected double sumOfSpeeds;

    protected CubicScheduling(RegularListGraph graph, VertexColoring coloring, double[] speeds) {
        if(graph != coloring.getGraph()) {
            throw new IllegalArgumentException(ILLEGAL_COLORING);
        }

        this.graph = graph;
        this.coloring = coloring;
        for(double i : speeds) sumOfSpeeds += i;
        setSpeeds(speeds);
    }

    public CubicScheduling(RegularListGraph graph, double[] speeds) {
        this(graph, new VertexColoring(graph), speeds);
    }

    public abstract VertexColoring findColoring();

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
