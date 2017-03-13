package scheduling.triple;

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

    public static final String ILLEGAL_COLORING = "Coloring must have reference to the same graph.";

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
        this.speeds = speeds;
    }

    public CubicScheduling(RegularListGraph graph, double[] speeds) {
        this(graph, new VertexColoring(graph), speeds);
    }

    public abstract VertexColoring findColoring();
}
