package scheduling.three;

import graph.VertexColoring;

import java.util.Arrays;
import java.util.LinkedList;

import static junit.framework.TestCase.assertTrue;
import static scheduling.three.Const.*;

/**
 * Created by Paweł Kopeć on 02.03.17.
 * <p>
 * Class implementing algorithm for decreasing
 * the width of coloring of cubic graph.
 */
class ClwWithConstantB {

    private VertexColoring coloring;
    private int verticesToMove;

    private ComponentSwapper swapper;
    private X3Y A3B, A3C, B3A, C3A, C3B;

    ClwWithConstantB(VertexColoring coloring) {
        this.coloring = coloring;

        swapper = new ComponentSwapper(coloring);

        A3B = new X3Y(coloring, A, B);
        A3C = new X3Y(coloring, A, C);
        B3A = new X3Y(coloring, B, A);
        C3A = new X3Y(coloring, C, A);
        C3B = new X3Y(coloring, C, B);
    }

    void tmp() {
        int[] colors = new int[]{coloring.getNumberOfColored(A), coloring.getNumberOfColored(B), coloring.getNumberOfColored(C)};
        System.out.println("Zostało do przeniesienia: " + verticesToMove);
        System.out.println("");
        System.out.println("|A3B| = " + A3B.getSize());
        System.out.println("|A3C| = " + A3C.getSize());
        System.out.println("|B3A| = " + B3A.getSize());
        System.out.println("|C3A| = " + C3A.getSize());
        System.out.println("|C3B| = " + C3B.getSize());
        System.out.println("");
        System.out.println("Kolory :" + Arrays.toString(colors));
        System.out.println("------------------------------------");
    }

    /**
     * Decrease the width of coloring by given number
     * without changing the size of middle color class.
     *
     * @param desiredVerticesToMove from biggest color class to smallest
     */
    void moveVertices(int desiredVerticesToMove) {
        this.verticesToMove = desiredVerticesToMove;
        tmp();

        while (0 < verticesToMove) {
            if (moveFromA3BToC()) {
                System.out.println("c <= a3b");
                tmp();
                continue;
            }

            if (swapBetweenAAndC()) {
                System.out.println("a <=> c");
                tmp();
                continue;
            }

            if (makeB3ANotEmpty()) {
                System.out.println("b3a filled");
                tmp();
                continue;
            }

            if (swapWithinA3CAndB3A()) {
                System.out.println("b <= a3c, c <= b3a");
                tmp();
                continue;
            }

            if (swapBetweenAAndBAndMoveToC()) {
                System.out.println("a <=> b, c <= b3a");
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

    /**
     * .
     * Decrease the coloring by simply moving all vertices
     * from A3B to C.
     *
     * @return true if width was decreased
     */
    private boolean moveFromA3BToC() {
        if (A3B.empty()) {
            return false;
        }

        verticesToMove -= swapper.changeColor(A3B.getVertices(), C, verticesToMove);

        B3A.setForUpdate();
        C3B.setForUpdate();

        return true;
    }

    /**
     * Try to decrease the coloring by swapping colors of components
     * in G(A, C) subgraph. If verticesToMove < |A'| - |C'|,
     * then compensate too big differences in sizes by moving
     * appropriate amount of vertices from C3B back to A.
     *
     * @return true if width was decreased
     */
    private boolean swapBetweenAAndC() {
        assertTrue(A3B.empty());
        if (1 < verticesToMove || C3A.empty() || (!C3B.empty() && B3A.empty())) {
            int verticesMoved = swapper.swap(A, C, verticesToMove, C3B.getVertices());
            if (0 < verticesMoved) {
                verticesToMove -= verticesMoved;
                A3C.setForUpdate();
                A3B.setForUpdate();
                B3A.setForUpdate();
                C3A.setForUpdate();

                return true;
            }
        }

        return false;
    }

    /**
     * If B3A is empty make it not empty. First swap colors
     * of components in G(A, B) subgraph. Then move vertices
     * from C3A to B in order to restore previous size
     * of B. Now vertices moved from C3A are in B3A.
     */
    private boolean makeB3ANotEmpty() {
        if (B3A.empty()) {
            swapper.swapWithoutDecreasing(B, C, C3A.getVertices());

            A3B.setForUpdate();
            A3C.setForUpdate();
            B3A.setForUpdate();
            C3A.setForUpdate();
            C3B.setForUpdate();

            return true;
        }

        return false;
    }

    /**
     * For every x in A3C that has a pair as y in B3A
     * move x to B and then move y to C to restore
     * previous size of B and decrease width of coloring.
     *
     * @return true if width was decreased
     */
    private boolean swapWithinA3CAndB3A() {
        if (A3C.getSize() == 0 || B3A.getSize() == 0) {
            return false;
        }

        LinkedList<Integer> A3CVertices = A3C.getVertices(), B3AVertices = B3A.getVertices();

        while (0 < A3C.getSize() && 0 < B3A.getSize() && 0 < verticesToMove) {
            coloring.set(A3CVertices.poll(), B);
            coloring.set(B3AVertices.poll(), C);
            verticesToMove--;
        }

        C3A.setForUpdate();

        return true;
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
        int decreasedBy = swapper.swapAndMoveUntilDecreased(A, B, C, B3A.getVertices(), verticesToMove);
        if (0 < decreasedBy) {
            verticesToMove -= decreasedBy;
            A3B.setForUpdate();
            B3A.setForUpdate();
            C3A.setForUpdate();

            return true;
        }

        return false;
    }
}
