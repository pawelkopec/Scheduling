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
     * Try to swap components between color classes
     * so that it will decrease the width of coloring.
     * If difference in sizes of the components are too big
     * compensate it by moving vertices from compensator.
     *
     * @param colorBig       color of bigger color class
     * @param colorSmall     color of smaller color class
     * @param verticesToMove desired change of the coloring width
     * @return how much the width was decreased
     */
    int swap(int colorBig, int colorSmall, int verticesToMove, LinkedList<Integer> compensator) {
        BitSet checked = new BitSet(graph.getVertices());
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        int currentColor, sizeDifference, toCompensate, verticesMoved = 0;

        for (int i = 0; i < graph.getVertices(); i++) {

            if (!checked.get(i) && 0 < verticesToMove) {

                checked.set(i);
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    findComponents(i, colorBig, colorSmall, bigComponent, smallComponent, checked);
                    sizeDifference = bigComponent.size() - smallComponent.size();
                    if (0 < sizeDifference && sizeDifference <= verticesToMove + compensator.size()) {
                        toCompensate = sizeDifference - verticesToMove;
                        toCompensate = 0 < toCompensate ? toCompensate : 0;
                        verticesMoved += sizeDifference - toCompensate;
                        verticesToMove -= sizeDifference - toCompensate;

                        changeColor(bigComponent, colorSmall);
                        changeColor(smallComponent, colorBig);
                        changeColor(compensator, colorBig, toCompensate);
                    }

                    bigComponent.clear();
                    smallComponent.clear();
                }
            }
        }

        return verticesMoved;
    }

    /**
     * Try to swap components between color classes
     * so that the sizes of color classes will remain the same.
     * Use vertices from compensator to compensate difference
     * in sizes of color classes.
     *
     * @param colorBig    color of bigger color class
     * @param colorSmall  color of smaller color class
     * @param compensator list of vertices used to compensate
     */
    void swapWithoutDecreasing(int colorBig, int colorSmall, LinkedList<Integer> compensator) {
        BitSet checked = new BitSet(graph.getVertices());
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        int currentColor, sizeDifference;

        for (int i = 0; i < graph.getVertices(); i++) {

            if (!checked.get(i)) {

                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    findComponents(i, colorBig, colorSmall, bigComponent, smallComponent, checked);
                    sizeDifference = bigComponent.size() - smallComponent.size();
                    if (0 < sizeDifference && sizeDifference <= compensator.size()) {
                        changeColor(bigComponent, colorSmall);
                        changeColor(smallComponent, colorBig);
                        changeColor(compensator, colorBig, sizeDifference);

                        return;
                    }
                }
            }
        }
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
        BitSet checked = new BitSet(graph.getVertices()), compensatorArray = new BitSet(graph.getVertices());
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>(), toRemoveFromCompensator = new LinkedList<>();
        int currentColor, sizeDifference = 0;

        /*
         * Rewrite compensator as bit array for
         * checking value in constant time.
         */
        for (Integer i : compensator) {
            compensatorArray.set(i);
        }

        for (int i = 0; i < graph.getVertices(); i++) {

            if (!checked.get(i) && 0 < verticesToMove) {

                checked.set(i);
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
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

                            changeColor(bigComponent, colorSmall);
                            changeColor(smallComponent, colorBig);
                            changeColor(compensatorArray, colorOther, sizeDifference);

                            return sizeDifference;
                        }
                    }

                    bigComponent.clear();
                    smallComponent.clear();
                    toRemoveFromCompensator.clear();
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

    private void changeColor(BitSet vertices, int color) {
        changeColor(vertices, color, vertices.size());
    }
}
