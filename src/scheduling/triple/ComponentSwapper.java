package scheduling.triple;

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
     * @param toDecrease desired change of coloring  width
     * @return how much width was decreased
     */
    int swapBetween(int colorBig, int colorSmall, int toDecrease) {
        BitSet checked = new BitSet(graph.getVertices());
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        int currentColor, sizeDifference;

        for(int i = 0; i < graph.getVertices(); i++) {

            if (!checked.get(i)) {

                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    findComponents(i, colorBig, colorSmall, bigComponent, smallComponent, checked);
                    sizeDifference = bigComponent.size() - smallComponent.size();
                    if (0 < sizeDifference && sizeDifference <= toDecrease) {
                        for (Integer index : bigComponent) {
                            coloring.set(index, colorSmall);
                        }
                        for (Integer index : smallComponent) {
                            coloring.set(index, colorBig);
                        }

                        checked.clear();

                        return sizeDifference;
                    }
                }
            }
        }

        return 0;
    }

    int swapBetweenAndCompensate(int colorBig, int colorSmall,
                                 LinkedList<Integer> compensator,
                                 int toDecrease) {
        //TODO
        return 0;
    }

    public int swapBetweenAndMoveToOther(int colorBig, int colorSmall, int colorOther,
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
            checked.set(current);

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
            }
        }
    }
}
