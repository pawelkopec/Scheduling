package scheduling.three;

import graph.RegularGraph;
import graph.VertexColoring;

import java.util.LinkedList;

import static scheduling.three.Const.A;
import static scheduling.three.Const.B;
import static scheduling.three.Const.C;

/**
 * Created by Paweł Kopeć on 02.03.17.
 * <p>
 * Class implementing algorithm for decreasing
 * the width of coloring of cubic graph.
 */
class ClwWithConstantB {

    private RegularGraph graph;
    private VertexColoring coloring;
    private int verticesToMove;

    private ComponentSwapper swapper;
    private X3Y A3B, A3C, B3A, C3A, C3B;

    ClwWithConstantB(RegularGraph graph, VertexColoring coloring) {
        this.graph = graph;
        this.coloring = coloring;

        swapper = new ComponentSwapper(coloring);

        A3B = new X3Y(A, B);
        A3C = new X3Y(A, C);
        B3A = new X3Y(B, A);
        C3A = new X3Y(C, A);
        C3B = new X3Y(C, B);
    }

    void tmp() {
        System.out.println("Zostało do przeniesienia: " + verticesToMove);
        System.out.println("|A3B| = " + A3B.getSize());
        System.out.println("|A3C| = " + A3C.getSize());
        System.out.println("|B3A| = " + B3A.getSize());
        System.out.println("|C3A| = " + C3A.getSize());
        System.out.println("|C3B| = " + C3B.getSize());
        System.out.println("------------------------------------");
    }

    /**
     * Decrease the width of coloring by
     *
     * @param desiredVerticesToMove
     */
    void moveVertices(int desiredVerticesToMove) {
        this.verticesToMove = desiredVerticesToMove;

        while (0 < verticesToMove) {
            tmp();
            if (moveFromA3BToC()) {
                tmp();
                continue;
            }

            if (swapBetweenAAndC()) {
                tmp();
                continue;
            }

            makeB3ANotEmpty();

            if (swapWithinA3CAndB3A()) {
                tmp();
                return;
            }

            if (swapBetweenAAndBAndMoveToC()) {
                tmp();
                continue;
            }
            return;
            /*if (true) { //TODO
                swapWithinA1BAndB3A();
                continue;
            } else if (true) { //TODO
                if (true) { //TODO
                    //TODO
                    continue;
                } else if (true) { //TODO
                    //TODO
                    continue;
                } else if (true) { //TODO
                    //TODO
                    continue;
                }
            }

            leaveOnlyPathsInAAndBSubstets();
            findProperPath();

            if (pathDisconnectedOnOneSide()) {
                //TODO
                continue;
            } else if (wAdjacentToASubset()) {
                //TODO
                continue;
            }

            crazySwap();*/

        }
    }

    /*
     * The following methods are made only to organize
     * the steps of an algorithm and make it more readable.
     *
     * They are useless if not used in the context of
     * decreaseBy(int) method.
     */

    /**.
     * Decrease the coloring by simply moving all vertices
     * in A3B to C.
     *
     * @return true if width was decreased
     */
    private boolean moveFromA3BToC() {
        if (A3B.empty()) {
            return false;
        }

        while (0 < verticesToMove && 0 < A3B.getSize()) {
            coloring.set(A3B.vertices.poll(), C);
            verticesToMove--;
        }

        B3A.upToDate = false;
        C3B.upToDate = false;

        return true;
    }

    /**
     * Decrease the coloring by swapping colors of components
     * in G(A, C) subgraph. If verticesToMove < |A'| - |C'|,
     * then compensate too big differences in sizes by moving
     * appropriate amount of vertices from C3B back to A.
     *
     * @return true if width was decreased
     */
    private boolean swapBetweenAAndC() {
        if (1 < verticesToMove || C3A.empty() || !C3B.empty()) {
            verticesToMove -= swapper.swapBetween(A, C, verticesToMove, C3B.vertices);
            A3C.upToDate = false;
            A3B.upToDate = false;
            B3A.upToDate = false;
            C3A.upToDate = false;

            return true;
        }

        return false;
    }

    /**
     * First swap colors of components in G(A, B) subgraph
     * to make A smaller and B bigger. Then move vertices
     * from B3A - B'3A' to C in order to restore previous size
     * of B and decrease the width.
     *
     * @return true if width was decreased
     */
    private boolean swapBetweenAAndBAndMoveToC() {
        B3A.update();
        int decreasedBy = swapper.swapBetweenAndMoveToOther(A, B, C, B3A.vertices, verticesToMove);
        if (0 < decreasedBy) {
            verticesToMove -= decreasedBy;
            return true;
        }

        return false;
    }

    /**
     * If B3A is empty make it not empty. First swap colors
     * of components in G(A, B) subgraph. Then move vertices
     * from C3A to B in order to restore previous size
     * of B. Now vertices moved from C3A are in B3A.
     */
    private void makeB3ANotEmpty() {
        if (B3A.empty()) {
            C3A.update();
            swapper.swapBetweenWithoutDecreasing(B, C, C3A.vertices);

            A3B.upToDate = false;
            A3C.upToDate = false;
            B3A.upToDate = false;
            C3A.upToDate = false;
            C3B.upToDate = false;
        }
    }

    private boolean swapWithinA3CAndB3A() {
        if (A3C.getSize() == 0 || B3A.getSize() == 0) {
            return false;
        }

        int x, y;
        while (0 < A3C.getSize() && 0 < B3A.getSize() && 0 < verticesToMove) {
            x = A3C.vertices.poll();
            y = B3A.vertices.poll();
            coloring.set(x, B);
            coloring.set(y, C);
            verticesToMove--;
        }

        return true;
    }

    private void swapWithinA1BAndB3A() {
        //TODO
    }

    private void leaveOnlyPathsInAAndBSubsets() {
        //TODO
    }

    private void findProperPath() {
        //TODO
    }

    private boolean pathDisconnectedOnOneSide() {
        //TODO
        return false;
    }

    private boolean wAdjacentToASubset() {
        //TODO
        return false;
    }

    private void crazySwap() {
        //TODO
    }

    /**
     * If X3Y.vertices contains index n,
     * it means that vertex n has a color X and
     * it has 3 neighbours of color Y.
     */
    private class X3Y {
        LinkedList<Integer> vertices = new LinkedList<>();

        private int colorX, colorY;

        private boolean upToDate;

        X3Y(int colorX, int colorY) {
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

        void update() {
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
    }
}
