package scheduling.three;

import graph.Graph;
import graph.VertexColoring;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Paweł Kopeć on 15.03.17.
 * <p>
 * Class used for swapping colors between components
 * within clw procedure.
 */
class ComponentSwapper {

    private Graph graph;
    private VertexColoring coloring;

    /**
     * Store colors as object field to avoid passing
     * huge amount of arguments to private methods.
     */
    private int colorBig, colorSmall, colorOther;

    ComponentSwapper(VertexColoring coloring) {
        this.coloring = coloring;
        this.graph = coloring.getGraph();
    }

    /**
     * Try to swap pairs of components X', Y' of induced graph G(X, Y)
     * where X has colorBig and Y has colorSmall as long as it is not
     * possible or desired amount of vertices has been moved to Y.
     *
     * @param colorBig       color of bigger color class
     * @param colorSmall     color of smaller color class
     * @param verticesToMove desired change of the coloring width
     * @return how much the width was decreased
     */
    int swap(int colorBig, int colorSmall, int verticesToMove, LinkedList<Integer> compensator) {
        this.colorBig = colorBig;
        this.colorSmall = colorSmall;

        BitSet checked = new BitSet(graph.getVertices());

        int currentColor, verticesMoved, verticesMovedTogether = 0;

        for (int i = 0; i < graph.getVertices() && 0 < verticesToMove; i++) {
            if (!checked.get(i) && 0 < verticesToMove) {
                checked.set(i);
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    verticesMoved = swapWithCompensating(i, checked, compensator, verticesToMove);
                    verticesMovedTogether += verticesMoved;
                    verticesToMove -= verticesMoved;
                }
            }
        }

        return verticesMovedTogether;
    }

    /**
     * Swap color classes of component of induced graph that
     * contains vertex of currentIndex.
     * If difference in sizes of color classes is too big
     * compensate it by moving vertices from compensator to smaller class.
     *
     * @param currentIndex   of starting point of finding color classes
     * @param checked        BitSet of vertices not in use
     * @param compensator    list of vertices that can be used to compensate
     * @param verticesToMove desired maximum difference of size of color classes
     * @return how many vertices were moved
     */
    private int swapWithCompensating(int currentIndex, BitSet checked, LinkedList<Integer> compensator, int verticesToMove) {
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        findComponents(currentIndex, bigComponent, smallComponent, checked);

        int sizeDifference = bigComponent.size() - smallComponent.size(), verticesMoved = 0;

        if (0 < sizeDifference && sizeDifference <= verticesToMove + compensator.size()) {
            int toCompensate = sizeDifference - verticesToMove;
            toCompensate = 0 < toCompensate ? toCompensate : 0;
            verticesMoved = sizeDifference - toCompensate;

            changeColor(bigComponent, colorSmall);
            changeColor(smallComponent, colorBig);
            changeColor(compensator, colorBig, toCompensate);
        }

        return verticesMoved;
    }

    /**
     * Try to swap components between color classes
     * so that the sizes of all color classes will remain the same.
     * Use vertices from compensator to compensate difference
     * in sizes of color classes.
     *
     * @param colorBig    color of bigger color class
     * @param colorSmall  color of smaller color class
     * @param compensator list of vertices used to compensate
     */
    void swapWithoutDecreasing(int colorBig, int colorSmall, LinkedList<Integer> compensator) {
        this.colorBig = colorBig;
        this.colorSmall = colorSmall;
        BitSet checked = new BitSet(graph.getVertices());
        int currentColor;

        for (int i = 0; i < graph.getVertices(); i++) {
            if (!checked.get(i)) {
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    if (swapAndRestore(i, checked, compensator)) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Swap color classes of component of induced graph that
     * contains vertex of currentIndex and restore sizes of color
     * classes by moving vertices from compensator to smaller color class.
     *
     * @param currentIndex of starting point of finding color classes
     * @param checked      BitSet of vertices not in use
     * @param compensator  list of vertices that can be used to compensate
     * @return true if swapping took place
     */
    private boolean swapAndRestore(int currentIndex, BitSet checked, LinkedList<Integer> compensator) {
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        findComponents(currentIndex, bigComponent, smallComponent, checked);

        int sizeDifference = bigComponent.size() - smallComponent.size();

        if (0 < sizeDifference && sizeDifference <= compensator.size()) {
            changeColor(bigComponent, colorSmall);
            changeColor(smallComponent, colorBig);
            changeColor(compensator, colorBig, sizeDifference);

            return true;
        }

        return false;
    }

    /**
     * Try to swap components between color classes
     * and move vertices in number of difference in sizes
     * of those components from compensator to the third
     * color class.
     *
     * @param colorBig       color of bigger color class
     * @param colorSmall     color of smaller color class
     * @param colorOther     color of the third color class
     * @param compensator    list of vertices used to compensate
     * @param verticesToMove desired change of the coloring width
     * @return how much the width was decreased
     */
    int swapAndMoveUntilDecreased(int colorBig, int colorSmall, int colorOther, LinkedList<Integer> compensator, int verticesToMove) {
        this.colorBig = colorBig;
        this.colorSmall = colorSmall;
        this.colorOther = colorOther;

        //TODO otherCompensator for compensating common neighbours
        BitSet checked = new BitSet(graph.getVertices());
        BooleanArray compensatorArray = vertexListToArray(compensator);
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        int currentColor, verticesMovedTotal = 0, verticesMoved;

        for (int i = 0; i < graph.getVertices() && 0 < verticesToMove; i++) {
            if (!checked.get(i)) {
                checked.set(i);
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    findComponents(i, bigComponent, smallComponent, checked);

                    verticesMoved = useComponentsToDecrease(bigComponent, smallComponent, compensatorArray, verticesToMove);
                    verticesMovedTotal += verticesMoved;
                    verticesToMove -= verticesMoved;

                    bigComponent.clear();
                    smallComponent.clear();
                }
            }
        }

        return verticesMovedTotal;
    }

    private int useComponentsToDecrease(LinkedList<Integer> bigComponent,
                                        LinkedList<Integer> smallComponent,
                                        BooleanArray compensator, int verticesToMove) {
        //TODO arguments
        LinkedList<Integer> small3Big = getFrom3To(smallComponent, colorBig);
        int decreasedBy = swapAndMoveToOther(bigComponent, smallComponent,
                small3Big, compensator, verticesToMove);

        if (0 < decreasedBy) {
            return decreasedBy;
        }

        decreasedBy = moveNeighbour(small3Big, compensator, verticesToMove);
        if (0 < decreasedBy) {
            return decreasedBy;
        }

        decreasedBy = moveCommonNeighbours(small3Big, compensator, verticesToMove);
        if (0 < decreasedBy) {
            return decreasedBy;
        }

        return reduceToPathsAndSwap();
    }

    private int swapAndMoveToOther(LinkedList<Integer> bigComponent,
                                   LinkedList<Integer> smallComponent,
                                   LinkedList<Integer> toRemoveFromCompensator,
                                   BooleanArray compensator, int verticesToMove) {
        //TODO
        int sizeDifference = bigComponent.size() - smallComponent.size();
        if (0 < sizeDifference && sizeDifference <= verticesToMove && sizeDifference <= compensator.getCount()) {
            if (sizeDifference <= compensator.getCount() - toRemoveFromCompensator.size()) {
                /*
                 * Remove newly forbidden vertices
                 * from compensator.
                 */
                for (Integer i : toRemoveFromCompensator) {
                    compensator.set(i, false);
                }

                /*
                 * Compensate swap that will take place
                 * in a moment using updated compensator.
                 */
                changeColor(compensator, colorOther, sizeDifference);

                /*
                 * Update compensator with vertices newly
                 * added to small color class.
                 */
                for (Integer i : getFrom3To(bigComponent, colorSmall)) {
                    compensator.set(i);
                }

                changeColor(bigComponent, colorSmall);
                changeColor(smallComponent, colorBig);
            }
        }

        return sizeDifference;
    }

    private int moveNeighbour(LinkedList<Integer> small3Big,
                              BooleanArray compensator, int verticesToMove) {
        //TODO
        Graph graph = coloring.getGraph();
        int verticesMoved = 0, neighboursInBig;
        for (Integer i : small3Big) {
            if (0 < verticesToMove) {
                for (Integer j: graph.getNeighbours(i)) {
                    neighboursInBig = 0;
                    for (Integer k: graph.getNeighbours(j)) {
                        if (coloring.get(k) == colorSmall) {
                            neighboursInBig++;
                        }
                    }

                    if(neighboursInBig == 1) {
                        coloring.set(i, colorOther);
                        coloring.set(j, colorBig);
                        compensator.set(i, false);

                        verticesMoved++;
                        verticesToMove--;

                        break;
                    }
                }
            }
            else {
                break;
            }
        }

        return verticesMoved;
    }

    private int moveCommonNeighbours(LinkedList<Integer> small3Big,
                                     BooleanArray compensator, int verticesToMove) {
        //TODO
        Graph graph = coloring.getGraph();
        int verticesMoved = 0, other = 0;
        boolean isCommonNeighbour;
        for (Integer i : small3Big) {
            if (0 < verticesToMove) {
                for (Integer j: graph.getNeighbours(i)) {
                    isCommonNeighbour = false;
                    for (Integer k: graph.getNeighbours(j)) {
                        if (X3Y.hasAllNeighboursInY(k, colorBig, coloring)) {
                            isCommonNeighbour = true;
                            other = k;
                            break;
                        }
                    }

                    if (isCommonNeighbour) {
                        //TODO arguments
                        if (moveCommonAndSpare(i, other, j)) {
                            compensator.set(i, false);
                            verticesMoved++;
                            verticesToMove--;
                        }

                        break;
                    }
                }
            }
            else {
                break;
            }
        }

        return verticesMoved;
    }

    private boolean moveCommonAndSpare(int x, int y, int z) {
        //TODO
        return false;
    }

    private int reduceToPathsAndSwap() {
        //TODO
        return 0;
    }

    /**
     * Find components within induced graph of
     * two given color starting from vertex with
     * current index.
     *
     * @param current        index to start with
     * @param bigComponent   container of indexes to fill
     * @param smallComponent container of indexes to fill
     */
    private void findComponents(int current, LinkedList<Integer> bigComponent,
                                LinkedList<Integer> smallComponent, BitSet checked) {
        Queue<Integer> queue = new LinkedList<>();
        int currentColor, otherColor;

        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.poll();

            currentColor = coloring.get(current);

            if (currentColor == colorBig) {
                otherColor = colorSmall;
                bigComponent.add(current);
            } else {
                otherColor = colorBig;
                smallComponent.add(current);
            }

            for (Integer neighbour : graph.getNeighbours(current)) {
                if (coloring.get(neighbour) == otherColor && !checked.get(neighbour)) {
                    queue.add(neighbour);
                }
                checked.set(neighbour);
            }
        }
    }

    /**
     * Change n vertices from vertices list to
     * given color and remove them from list.
     *
     * @param vertices list of available vertices
     * @param color    that vertices will now have
     * @param n        number of vertices to be changed
     * @return number of vertices changed
     */
    int changeColor(LinkedList<Integer> vertices, int color, int n) {
        int changed = 0;
        for (int i = 0; 0 < vertices.size() && changed < n; i++) {
            coloring.set(vertices.poll(), color);
            changed++;
        }

        return changed;
    }

    private void changeColor(LinkedList<Integer> vertices, int color) {
        changeColor(vertices, color, vertices.size());
    }

    private int changeColorWithoutPolling(LinkedList<Integer> vertices, int color, int n) {
        int changed = 0;
        for (Integer i : vertices) {
            coloring.set(i, color);
            if(n <= ++changed) {
                break;
            }
        }

        return changed;
    }

    /**
     * Change n vertices that have 1 assigned in the bit set
     * to a given color.
     *
     * @param vertices bit set with 1 for all available vertices
     * @param color    that vertices will now have
     * @param n        number of vertices to change color
     */
    private void changeColor(BitSet vertices, int color, int n) {
        for (int i = 0; i < vertices.size() && 0 < n; i++) {
            if (vertices.get(i)) {
                coloring.set(i, color);
                n--;
            }
        }
    }

    /**
     * Set all the bits representing vertices in list to 1.
     *
     * @param list of vertices to put in BitSet
     * @return BooleanArray representing list
     */
    private BooleanArray vertexListToArray(LinkedList<Integer> list) {
        BooleanArray booleanArray = new BooleanArray(graph.getVertices());

        for (Integer i : list) {
            booleanArray.set(i);
        }

        return booleanArray;
    }

    private LinkedList<Integer> getFrom3To(LinkedList<Integer> fromComponent, int toColor) {
        LinkedList<Integer> big3Small = new LinkedList<>();
        for (Integer i : fromComponent) {
            if (X3Y.hasAllNeighboursInY(i, toColor, coloring)) {
                big3Small.add(i);
            }
        }

        return big3Small;
    }
}
