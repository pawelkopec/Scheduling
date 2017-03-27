package scheduling.three;

import graph.Graph;
import graph.VertexColoring;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Paweł Kopeć on 15.03.17.
 *
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
     *
     * @param colorBig color of bigger color class
     * @param colorSmall color of smaller color class
     * @param verticesToMove desired change of coloring  width
     * @return how much width was decreased
     */
    int swapBetween(int colorBig, int colorSmall, int verticesToMove, LinkedList<Integer> compensator) {
        BitSet checked = new BitSet(graph.getVertices());
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        int currentColor, sizeDifference, toCompensate, verticesMoved = 0;

        for(int i = 0; i < graph.getVertices(); i++) {

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

                        swapComponents(bigComponent, smallComponent, colorBig, colorSmall);
                        compensate(compensator, colorBig, toCompensate);
                    }

                    bigComponent.clear();
                    smallComponent.clear();
                }
            }
        }

        return verticesMoved;
    }

    void swapBetweenWithoutDecreasing(int colorBig, int colorSmall, LinkedList<Integer> compensator) {
        BitSet checked = new BitSet(graph.getVertices());
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        int currentColor, sizeDifference;

        for(int i = 0; i < graph.getVertices(); i++) {

            if (!checked.get(i)) {

                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    findComponents(i, colorBig, colorSmall, bigComponent, smallComponent, checked);
                    sizeDifference = bigComponent.size() - smallComponent.size();
                    if (0 < sizeDifference && sizeDifference <= compensator.size()) {
                        swapComponents(bigComponent, smallComponent, colorBig, colorSmall);
                        compensate(compensator, colorBig, sizeDifference);

                        return;
                    }
                }
            }
        }
    }

    int swapBetweenAndMoveToOther(int colorBig, int colorSmall, int colorOther,
                                         LinkedList<Integer> compensator,
                                         int toDecrease) {
        //TODO
        return 0;
    }

    /**
     * Find components within two given colors
     * starting from vertex with current index.
     *
     * @param current index to start with
     * @param colorBig color of bigger color class
     * @param colorSmall color of smaller color class
     * @param bigComponent list of indexes to fill
     * @param smallComponent list of indexes to fill
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
            }
            else {
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

    private void swapComponents(LinkedList<Integer> x, LinkedList<Integer> y, int colorX, int colorY) {
        for (Integer i : x) {
            coloring.set(i, colorY);
        }
        for (Integer i : y) {
            coloring.set(i, colorX);
        }
    }

    private void compensate(LinkedList<Integer> compensator, int color, int n) {
        for (int i = 0; i < n; i++) {
            coloring.set(compensator.poll(), color);
        }
    }
}
