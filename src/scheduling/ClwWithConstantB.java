package scheduling;

import graph.RegularListGraph;
import graph.VertexColoring;

import java.util.Iterator;
import java.util.LinkedList;

import static scheduling.Const.A;
import static scheduling.Const.B;
import static scheduling.Const.C;

/**
 * Created by Paweł Kopeć on 02.03.17.
 *
 * Class implementing algorithm for decreasing
 * the width of coloring of cubic graph.
 */
public class ClwWithConstantB {

    private RegularListGraph graph;
    private VertexColoring coloring;
    private int toDecrease;

    /**
     * Index contained by XnY means that this vertex
     * is in X and is adjacent to n vertices in Y.
     */
    private LinkedList<Integer> A3B, A3C, B3A, C3A, C3B;

    private boolean A3BUpTodate, A3CUpTodate, B3AUpTodate, C3AUpTodate, C3BUpTodate;

    public ClwWithConstantB(RegularListGraph graph, VertexColoring coloring) {
        this.graph = graph;
        this.coloring = coloring;

        A3B = new LinkedList<>();
        C3B = new LinkedList<>();
        A3C = new LinkedList<>();
    }

    public void decreaseBy(int toDecrease) {
        this.toDecrease = toDecrease;

        while (toDecrease > 0) {
            if (!A3BEmpty()) {
                moveFromA3BToC();
                continue;
            }
            else if (C3AEmpty()) {
                swapAAndCComponents();
                continue;
            }
            else if (B3AEmpty()) {
                if(true) { //TODO
                    //TODO
                    continue;
                }
                else{
                    makeB3ANotEmpty();
                }
            }

            if (!A3CEmpty()) {
                swapWithinA3CAndB3A();
                continue;
            }
            else if(true) { //TODO
                swapAAndBComponentsAndCompensate();
                continue;
            }
            else if (true) { //TODO
                swapWithinA1BAndB3A();
                continue;
            }
            else if(true) { //TODO
                if (true) { //TODO
                    //TODO
                    continue;
                }
                else if(true) { //TODO
                    //TODO
                    continue;
                }
                else if (true) { //TODO
                    //TODO
                    continue;
                }
            }

            leaveOnlyPathsInAAndBSubstets();
            findProperPath();

            if (pathDisconnectedOnOneSide()) {
                //TODO
                continue;
            }
            else if (wAdjacentToASubset()) {
                //TODO
                continue;
            }

            crazySwap();

        }
    }

    private void moveFromA3BToC() {
        Iterator<Integer> it = A3B.iterator();

        while (0 < toDecrease && it.hasNext()) {
            coloring.set(it.next(), C);
            it.remove();
            toDecrease--;
        }
    }

    private void swapAAndCComponents() {
        //TODO
    }

    private void makeB3ANotEmpty() {
        //TODO
    }

    private void swapWithinA3CAndB3A() {
        //TODO
    }

    private void swapAAndBComponentsAndCompensate() {
        //TODO
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

    private boolean A3BEmpty() {
        if (!A3BUpTodate) {
            updateX3Y(A3B, A, B);
            A3BUpTodate = true;
        }

        return A3B.size() == 0;
    }

    private boolean A3CEmpty() {
        if (!A3CUpTodate) {
            updateX3Y(A3C, A, C);
            A3CUpTodate = true;
        }

        return A3C.size() == 0;
    }

    private boolean B3AEmpty() {
        if (!B3AUpTodate) {
            updateX3Y(B3A, B, C);
            B3AUpTodate = true;
        }

        return B3A.size() == 0;
    }

    private boolean C3AEmpty() {
        if (!C3AUpTodate) {
            updateX3Y(C3A, C, A);
            C3AUpTodate = true;
        }

        return C3A.size() == 0;
    }

    private void updateX3Y(LinkedList<Integer> set, int xColor, int yColor) {
        LinkedList<Integer> neighbours;
        for(int i = 0; i < graph.getVertices(); i++) {
            if (i == xColor) {
                neighbours = graph.getNeighbours(i);
                for (Integer n : neighbours) {
                    if (coloring.get(n) != yColor) {
                        continue;
                    }
                    set.push(n);
                }
            }
        }
    }
}
