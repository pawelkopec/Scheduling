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
        BitSet checked = new BitSet(graph.getVertices());

        int currentColor, verticesMoved, verticesMovedTogether = 0;

        for (int i = 0; i < graph.getVertices() && 0 < verticesToMove; i++) {
            if (!checked.get(i) && 0 < verticesToMove) {
                checked.set(i);
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    verticesMoved = swapWithCompensating(i, colorBig, colorSmall, checked, compensator, verticesToMove);
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
     * @param colorBig       color of bigger color class
     * @param colorSmall     color of smaller color class
     * @param checked        BitSet of vertices not in use
     * @param compensator    list of vertices that can be used to compensate
     * @param verticesToMove desired maximum difference of size of color classes
     * @return how many vertices were moved
     */
    private int swapWithCompensating(int currentIndex, int colorBig, int colorSmall, BitSet checked, LinkedList<Integer> compensator, int verticesToMove) {
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        findComponents(currentIndex, colorBig, colorSmall, bigComponent, smallComponent, checked);

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
        BitSet checked = new BitSet(graph.getVertices());
        int currentColor;

        for (int i = 0; i < graph.getVertices(); i++) {
            if (!checked.get(i)) {
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    if (swapAndRestore(i, colorBig, colorSmall, checked, compensator)) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Swap color classes of component of induced graph that
     * contains vertex of currentIndex and restore sizes of color
     * classes by moving vertices from compensator to smaller class.
     *
     * @param currentIndex of starting point of finding color classes
     * @param colorBig     color of bigger color class
     * @param colorSmall   color of smaller color class
     * @param checked      BitSet of vertices not in use
     * @param compensator  list of vertices that can be used to compensate
     * @return true if swapping took place
     */
    private boolean swapAndRestore(int currentIndex, int colorBig, int colorSmall, BitSet checked, LinkedList<Integer> compensator) {
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        findComponents(currentIndex, colorBig, colorSmall, bigComponent, smallComponent, checked);

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
    int swapAndMoveToOther(int colorBig, int colorSmall, int colorOther, LinkedList<Integer> compensator, int verticesToMove) {
        BitSet checked = new BitSet(graph.getVertices()), compensatorArray = listToBitSet(compensator);
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>(), toRemoveFromCompensator = new LinkedList<>();
        int currentColor, sizeDifference, verticesMoved = 0, compensatorSize = compensator.size();

        for (int i = 0; i < graph.getVertices() && 0 < verticesToMove; i++) {
            if (!checked.get(i)) {
                checked.set(i);
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    //TODO copy of compensatorArray
                    findComponents(i, colorBig, colorSmall, bigComponent, smallComponent, checked);
                    sizeDifference = bigComponent.size() - smallComponent.size();
                    if (0 < sizeDifference && sizeDifference <= verticesToMove) {
                        /*
                         * Remove all the vertices from compensator that
                         * cannot be used to compensate anymore, because they
                         * are in components to be swapped.
                         */
                        for (Integer j : smallComponent) {
                            if (compensatorArray.get(i)) {
                                toRemoveFromCompensator.add(i);
                                if (compensator.size() - toRemoveFromCompensator.size() < sizeDifference) {
                                    break;
                                }
                            }
                        }

                        if (sizeDifference <= compensator.size() - toRemoveFromCompensator.size()) {
                            for (Integer j : toRemoveFromCompensator) {
                                compensatorArray.set(i, false);
                            }
                            //TODO update compensator

                            changeColor(bigComponent, colorSmall);
                            changeColor(smallComponent, colorBig);
                            changeColor(compensatorArray, colorOther, sizeDifference);

                            verticesMoved += sizeDifference;
                            verticesToMove -= sizeDifference;
                        } else {
                            //TODO other cases
                        }
                    }

                    bigComponent.clear();
                    smallComponent.clear();
                    toRemoveFromCompensator.clear();
                }
            }
        }
        return verticesMoved;
    }

    private int swapAndMoveToOther(int colorBig, int colorSmall, int colorOther, int compensatorSize, BitSet compensator,
                                   LinkedList<Integer> bigComponent, LinkedList<Integer> smallComponent, int verticesToMove) {

        int sizeDifference = bigComponent.size() - smallComponent.size();
        if (0 < sizeDifference && sizeDifference <= verticesToMove && sizeDifference <= compensatorSize) {
            LinkedList<Integer> toRemoveFromCompensator = new LinkedList<>();
            for (Integer i : smallComponent) {
                if (X3Y.hasAllNeighboursInY(i, colorBig, coloring)) {
                    toRemoveFromCompensator.add(i);
                }
            }

            if (sizeDifference <= compensator.size() - toRemoveFromCompensator.size()) {
                for (Integer i : toRemoveFromCompensator) {
                    compensator.set(i, false);
                }

                changeColor(bigComponent, colorSmall);
                changeColor(smallComponent, colorBig);
                changeColor(compensator, colorOther, sizeDifference);

                for (Integer i : bigComponent) {
                    if (X3Y.hasAllNeighboursInY(i, colorSmall, coloring)) {
                        toRemoveFromCompensator.add(i);
                    }
                }
            }
        }

        return sizeDifference;
    }

    /**
     * Find components within induced graph of
     * two given color starting from vertex with
     * current index.
     *
     * @param current        index to start with
     * @param colorBig       color of bigger color class
     * @param colorSmall     color of smaller color class
     * @param bigComponent   container of indexes to fill
     * @param smallComponent container of indexes to fill
     */
    private void findComponents(int current, int colorBig, int colorSmall,
                                LinkedList<Integer> bigComponent,
                                LinkedList<Integer> smallComponent,
                                BitSet checked) {
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
     * @return BitSet representing list
     */
    private BitSet listToBitSet(LinkedList<Integer> list) {
        BitSet bitSet = new BitSet(graph.getVertices());

        for (Integer i : list) {
            bitSet.set(i);
        }

        return bitSet;
    }
}