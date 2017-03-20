package scheduling.triple;

import graph.RegularGraph;
import graph.RegularListGraph;
import graph.VertexColoring;

/**
 * Created by Paweł Kopeć on 02.03.17.
 *
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines on 3-chromatic cubic graph.
 * It delegates scheduling to two possible algorithms.
 */
class TricubicScheduling extends CubicScheduling {

    public TricubicScheduling(RegularGraph graph, VertexColoring coloring, double[] speeds) {
        super(graph, coloring, speeds);
    }

    public TricubicScheduling(RegularListGraph graph, double[] speeds) {
        super(graph, speeds);
    }

    public VertexColoring findColoring() {
        //TODO
        return coloring;
    }
}
