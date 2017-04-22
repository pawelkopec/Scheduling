package graph.util;

import graph.Graph;
import graph.RegularGraph;
import graph.RegularGraphFactory;

import java.util.ArrayList;
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
    protected <G extends RegularGraph> G getGraphA1(Class<G> clazz, int verticesNumber, int degree) {
        G g;
        do {
            g = RegularGraphFactory.getInstance(clazz, verticesNumber, degree);
            /*
             * Start with nd points {1, 2, ..., nd} (nd even) in verticesNumber groups.
             * Put points = {1, 2, ..., nd} (points denotes the set of unpaired points.)
             */
            ArrayList<Integer> pointsColor1 = new ArrayList<>(verticesNumber * degree / 2);
            ArrayList<Integer> pointsColor2 = new ArrayList<>(verticesNumber * degree / 2);
            int color;
            for (int i = 0, color1Size = 0, color2Size = 0; i < verticesNumber; i++) {
                color = rand.nextInt(2);
                if (verticesNumber / 2 <= color1Size && color == 0) {
                    color++;
                }

                if (verticesNumber / 2 <= color2Size && color == 1) {
                    color--;
                }

                if (color == 0) {
                    color1Size++;
                    for (int j = 0; j < degree; j++) {
                        pointsColor1.add(i);
                    }
                }
                else {
                    color2Size++;
                    for (int j = 0; j < degree; j++) {
                        pointsColor2.add(i);
                    }
                }
            }

            int indexFrom, indexTo, from, to, pointsLeft = verticesNumber * degree / 2;
            while (0 < pointsLeft) {
                indexFrom = rand.nextInt(pointsLeft);
                indexTo = rand.nextInt(pointsLeft);
                from = pointsColor1.get(indexFrom);
                to = pointsColor2.get(indexTo);

                if (!g.hasEdge(from, to)) {
                    g.addEdge(from, to);

                    pointsLeft--;
                    pointsColor1.set(indexFrom, pointsColor1.get(pointsLeft));
                    pointsColor2.set(indexTo, pointsColor2.get(pointsLeft));
                }

                if (pointsLeft == 1 &&
                    (pointsColor1.get(0) == pointsColor2.get(0) ||
                    g.hasEdge(pointsColor1.get(0), pointsColor2.get(0)))) {
                    break;
                }
            }

        } while (!g.isRegular(degree) || !Graph.isConnected(g));

        return g;
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
