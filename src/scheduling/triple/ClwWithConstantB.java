package scheduling.triple;

import graph.RegularGraph;
import graph.VertexColoring;

import java.util.Iterator;
import java.util.LinkedList;

import static scheduling.triple.Const.*;

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
        C3B = new X3Y(C, B);
        A3C = new X3Y(A, C);
        B3A = new X3Y(B, A);
        C3A = new X3Y(C, A);
        C3B = new X3Y(C, B);
    }

    /**
     * Decrease the width of coloring by
     *
     * @param verticesToMove
     */
    void moveVertices(int verticesToMove) {
        this.verticesToMove = verticesToMove;

        while (0 < verticesToMove) {
            if (moveFromA3BToC()) {
                System.out.println("from a3b to c");
                continue;
            }

            if (swapBetweenAAndC()) {
                System.out.println("a <=> c");
                return;
            }

            if (swapBetweenAAndCAndCompensate()) {
                System.out.println("a <=> c and compensate");
                continue;
            }

            makeB3ANotEmpty();

            if (swapWithinA3CAndB3A()) {
                continue;
            }

            if (swapBetweenAAndBAndMoveToC()) {
                continue;
            }
            verticesToMove--;
            continue;
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

    private boolean moveFromA3BToC() {
        if (A3B.empty()) {
            return false;
        }

        Iterator<Integer> it = A3B.vertices.iterator();
        int i;
        B3A.upToDate = false;

        while (0 < verticesToMove && it.hasNext()) {
            i = it.next();
            coloring.set(i, C);
            it.remove();
            C3B.vertices.add(i);
            verticesToMove--;
        }

        return true;
    }

    private boolean swapBetweenAAndC() {
        if (C3A.empty()) {
            verticesToMove -= swapper.swapBetween(A, C, verticesToMove);
            A3C.upToDate = false;
            B3A.upToDate = false;
            C3A.upToDate = false;

            return true;
        }

        return false;
    }

    private boolean swapBetweenAAndCAndCompensate(){
        if (B3A.empty()) {
            C3B.update();
            int decreasedBy = swapper.swapBetweenAndCompensate(A, C, C3B.vertices, verticesToMove);
            if (0 < decreasedBy) {
                verticesToMove -= decreasedBy;
                A3B.upToDate = false;
                A3C.upToDate = false;
                B3A.upToDate = false;
                C3B.upToDate = false;
                C3A.upToDate = false;

                return true;
            }
        }

        return false;
    }

    private boolean swapBetweenAAndBAndMoveToC() {
        B3A.update();
        int decreasedBy = swapper.swapBetweenAndMoveToOther(A, B, C, B3A.vertices, verticesToMove);
        if (0 < decreasedBy) {
            verticesToMove -= decreasedBy;
            return true;
        }

        return false;
    }

    private void makeB3ANotEmpty() {
        if(coloring.getNumberOfColored(B) == coloring.getNumberOfColored(C)) {
            for (int i = 0; i < graph.getVertices(); i++) {
                if (coloring.get(i) == C) {
                    coloring.set(i, B);
                }
                if (coloring.get(i) == B) {
                    coloring.set(i, C);
                }
            }
        }
        else {
            //TODO update B3A + swap
            C3A.upToDate = false;
        }
        A3B.upToDate = false;
        A3C.upToDate = false;
        C3B.upToDate = false;
    }

    private boolean swapWithinA3CAndB3A() {
        A3C.update();
        B3A.update();

        if(A3C.vertices.size() == 0 || B3A.vertices.size() == 0) {
            return false;
        }

        C3A.update();
        int x, y;
        while (0 < A3C.vertices.size() && 0 < B3A.vertices.size() && 0 < verticesToMove) {
            x = A3C.vertices.poll();
            y = B3A.vertices.poll();
            coloring.set(x, B);
            coloring.set(y, C);
            C3A.vertices.add(y);
            verticesToMove--;
        }

        return true;
    }

    private void swapWithinA1BAndB3A() {
        //TODO
    }

    private void leaveOnlyPathsInAAndBSubstets() {
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
            if (!upToDate) {
                update();
                upToDate = true;
            }

            return vertices.size() == 0;
        }

        void update() {
            if (!upToDate) {
                vertices.clear();
                LinkedList<Integer> neighbours;
                for (int i = 0; i < graph.getVertices(); i++) {
                    if (i == colorX) {
                        neighbours = graph.getNeighbours(i);
                        for (Integer n : neighbours) {
                            if (coloring.get(n) != colorY) {
                                continue;
                            }
                            vertices.add(i);
                        }
                    }
                }
            }
        }
    }
}
