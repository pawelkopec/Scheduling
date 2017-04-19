package scheduling.three;

import graph.Graph;
import graph.VertexColoring;

import java.util.LinkedList;

import static scheduling.three.Const.NO_VERTEX;

/**
 * Created by vivace on 04.04.17.
 * <p>
 * X3Y represents a set of vertices in cubic graph
 * that have color X and exactly 3 neighbours of color Y.
 * It is updated lazily. User can specify if the set needs to be updated
 */
class X3Y {

    private Graph graph;
    private VertexColoring coloring;
    private LinkedList<Integer> vertices = new LinkedList<>();

    private int colorX, colorY;

    private boolean upToDate;

    X3Y(VertexColoring coloring, int colorX, int colorY) {
        this.coloring = coloring;
        this.graph = coloring.getGraph();
        this.colorX = colorX;
        this.colorY = colorY;
    }

    boolean empty() {
        return getSize() == 0;
    }

    int getSize() {
        update();
        return vertices.size();
    }

    LinkedList<Integer> getVertices() {
        update();
        return vertices;
    }

    void setForUpdate() {
        upToDate = false;
    }

    private void update() {
        if (!upToDate) {
            vertices.clear();
            int neighboursWithOtherColor;
            for (int i = 0; i < graph.getVertices(); i++) {
                if (coloring.get(i) == colorX) {
                    neighboursWithOtherColor = 0;
                    for (Integer n : graph.getNeighbours(i)) {
                        if (coloring.get(n) == colorY) {
                            neighboursWithOtherColor++;
                        }
                    }

                    if (neighboursWithOtherColor == 3) {
                        vertices.add(i);
                    }
                }
            }

            upToDate = true;
        }
    }

    static int findOneInX3Y(int colorX, int colorY, VertexColoring coloring) {
        for (int i = 0; i < coloring.getGraph().getVertices(); i++) {
            if (coloring.get(i) == colorX && has3NeighboursInY(i, colorY, coloring)) {
                return i;
            }
        }

        return NO_VERTEX;
    }

    static int getNeighboursInY(int index, int colorY, VertexColoring coloring) {
        int neighboursCount = 0;
        for (Integer n : coloring.getGraph().getNeighbours(index)) {
            if (coloring.get(n) == colorY) {
                neighboursCount++;
            }
        }

        return neighboursCount;
    }

    static boolean has3NeighboursInY(int index, int colorY, VertexColoring coloring) {
        return getNeighboursInY(index, colorY, coloring) == 3;
    }


}