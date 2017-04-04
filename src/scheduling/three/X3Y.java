package scheduling.three;

import graph.Graph;
import graph.VertexColoring;

import java.util.LinkedList;

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

    static boolean hasAllNeighboursInY(int index, int colorY, VertexColoring coloring) {
        for (Integer n : coloring.getGraph().getNeighbours(index)) {
            if (coloring.get(n) != colorY) {
                return false;
            }
        }

        return true;
    }
}