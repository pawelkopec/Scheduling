package scheduling.three;

import graph.Graph;
import graph.RegularGraph;
import graph.VertexColoring;

/**
 * Created by Paweł Kopeć on 02.03.17.
 * <p>
 * Base class for finding coloring of
 * cubic incompatibility graphs.
 */
abstract class CubicScheduling {

    private static final String NULL_COLORING = "Coloring cannot be null.";

    protected Graph graph;
    protected VertexColoring coloring;
    double[] speeds;
    double sumOfSpeeds;
    int[] division = new int[3];

    CubicScheduling(VertexColoring coloring, double[] speeds) {
        if (coloring == null) {
            throw new IllegalArgumentException(NULL_COLORING);
        }

        this.graph = coloring.getGraph();
        this.coloring = coloring;
        for (double i : speeds) sumOfSpeeds += i;
        this.speeds = speeds;
    }

    public abstract VertexColoring findColoring();

    public int[] getDivision() {
        return division;
    }
}
