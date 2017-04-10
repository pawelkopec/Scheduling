package scheduling.three;

import graph.Graph;
import graph.RegularListGraph;
import graph.VertexColoring;

/**
 * Created by Paweł Kopeć on 02.03.17.
 * <p>
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines on 3-chromatic cubic graph.
 * It delegates scheduling to two possible algorithms.
 */
class TricubicScheduling extends CubicScheduling {

    TricubicScheduling(Graph graph, VertexColoring coloring, double[] speeds) {
        super(coloring, speeds);
    }

    public VertexColoring findColoring() {
        //TODO
        return coloring;
    }

    @Override
    public int[] getDivision() {
        return new int[0];
    }
}
