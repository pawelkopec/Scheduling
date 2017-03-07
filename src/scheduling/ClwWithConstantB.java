package scheduling;

import graph.RegularListGraph;
import graph.VertexColoring;

/**
 * Created by Paweł Kopeć on 02.03.17.
 *
 * Class implementing algorithm for decreasing
 * the width of coloring of cubic graph.
 */
public class ClwWithConstantB {

    private RegularListGraph graph;
    private VertexColoring coloring;

    public ClwWithConstantB(RegularListGraph graph, VertexColoring coloring) {
        this.graph = graph;
        this.coloring = coloring;
    }

    public void decreaseBy(int toDecrease) {
        while (toDecrease > 0) {

        }
    }
}
