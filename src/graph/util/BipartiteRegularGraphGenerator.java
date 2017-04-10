package graph.util;

import graph.RegularGraph;

import java.util.List;

/**
 * Created by robert on 02.04.17.
 */
public class BipartiteRegularGraphGenerator extends RegularGraphGenerator {
    private static final String INVALID_DEGREE_CEIL = "Number of vertices must be at least two times greater than degree.";

    @Override
    public <G extends RegularGraph> G getRandomGraph(Class<G> clazz,
                                                     int verticesNumber, int degree, ALGORITHMS algorithms) throws IllegalArgumentException {
        if (!(verticesNumber >= 2*degree)) {
            throw new IllegalArgumentException(INVALID_DEGREE_CEIL);
        }

        return super.getRandomGraph(clazz, verticesNumber, degree, algorithms);
    }

    @Override
    protected boolean areSuitable(int a, int b, List<Integer> pairs, int verticesNumber, int degree) {
        int aGroup = getPointGroup(a, verticesNumber);
        int bGroup = getPointGroup(b, verticesNumber);

        if (getGroupPartition(aGroup) == getGroupPartition(bGroup)) {
            return false;
        }

        return super.areSuitable(a, b, pairs, verticesNumber, degree);
    }

    protected int getGroupPartition(int group) {
        return group % 2;
    }
}
