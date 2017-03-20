package scheduling.triple;

import graph.RegularGraph;
import graph.VertexColoring;

import java.util.Arrays;

/**
 * Created by Paweł Kopeć on 02.03.17.
 *
 * Base class for finding coloring of
 * cubic incompatibility graphs.
 */
abstract class CubicScheduling {

    private static final String ILLEGAL_COLORING = "Coloring must have reference to the same graph.";

    protected RegularGraph graph;
    protected VertexColoring coloring;
    protected double[] speeds;
    protected double sumOfSpeeds;

    protected CubicScheduling(RegularGraph graph, VertexColoring coloring, double[] speeds) {
        if(graph != coloring.getGraph()) {
            throw new IllegalArgumentException(ILLEGAL_COLORING);
        }

        this.graph = graph;
        this.coloring = coloring;
        for(double i : speeds) sumOfSpeeds += i;
        this.speeds = speeds;
    }

    public CubicScheduling(RegularGraph graph, double[] speeds) {
        this(graph, new VertexColoring(graph), speeds);
    }

    public abstract VertexColoring findColoring();
}
