package scheduling.three;

import graph.Graph;
import graph.VertexColoring;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;

import static scheduling.three.Const.NO_VERTEX;

/**
 * Created by Paweł Kopeć on 15.03.17.
 * <p>
 * Class used for swapping colors between components
 * within clw procedure.
 *
 * All the confusing names like compensator, x, y, w
 * are used to match the names in article where unmodified clw
 * procedure is described. Without reading the article, methods of
 * the class below would not be understandable anyway.
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
    private int swapWithCompensating(int currentIndex, BitSet checked,
                                     LinkedList<Integer> compensator, int verticesToMove) {

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
    int swapAndMoveUntilDecreased(int colorBig, int colorSmall, int colorOther,
                                  LinkedList<Integer> compensator, int verticesToMove) {
        this.colorBig = colorBig;
        this.colorSmall = colorSmall;
        this.colorOther = colorOther;

        BitSet checked = new BitSet(graph.getVertices());
        BooleanArray compensatorArray = vertexListToArray(compensator);
        LinkedList<Integer> bigComponent = new LinkedList<>(), smallComponent = new LinkedList<>();
        int currentColor, verticesMoved = 0;

        for (int i = 0; i < graph.getVertices() && 0 < verticesToMove; i++) {
            if (!checked.get(i)) {
                checked.set(i);
                currentColor = coloring.get(i);

                if (currentColor == colorBig || currentColor == colorSmall) {
                    findComponents(i, bigComponent, smallComponent, checked);

                    if (bigComponent.size() - smallComponent.size() < 1) {
                        continue;
                    }

                    verticesMoved = useComponentsToDecrease(bigComponent, smallComponent,
                            compensatorArray, verticesToMove);

                    if (0 < verticesMoved) {
                        return verticesMoved;
                    }

                    bigComponent.clear();
                    smallComponent.clear();
                }
            }
        }

        return verticesMoved;
    }

    private int useComponentsToDecrease(LinkedList<Integer> bigComponent,
                                        LinkedList<Integer> smallComponent,
                                        BooleanArray compensator, int verticesToMove) {

        LinkedList<Integer> small3Big = getFrom3To(smallComponent, colorBig);
        int w;
        int decreasedBy = swapAndMoveToOther(bigComponent, smallComponent,
                small3Big, compensator, verticesToMove);

        if (0 < decreasedBy) {
            return decreasedBy;
        }

        decreasedBy = moveSoleNeighbours(small3Big, compensator, verticesToMove);
        if (0 < decreasedBy) {
            return decreasedBy;
        }

        /*
         * Now we know that all vertices in bigComponent
         * have exactly 2 neighbours in smallComponent.
         */

        w = X3Y.findOneInX3Y(colorOther, colorBig, coloring);

        if (moveCommonNeighbours(w, small3Big, compensator)) {
            return ++decreasedBy;
        }

        //TODO another vertex from other3Big

        return reduceToPathsAndSwap(w, bigComponent);
    }

    /**
     * Swap components and recover size of smallComponent by
     * moving vertices that are in compensator, but not
     * in component to colorOther class.
     *
     * @param bigComponent            of bigColor class
     * @param smallComponent          of smallColor class
     * @param toRemoveFromCompensator vertices that are also in smallComponent
     * @param compensator             list of vertices used to compensate
     * @param verticesToMove          desired change of the coloring width
     * @return how much the width was decreased
     */
    private int swapAndMoveToOther(LinkedList<Integer> bigComponent,
                                   LinkedList<Integer> smallComponent,
                                   LinkedList<Integer> toRemoveFromCompensator,
                                   BooleanArray compensator, int verticesToMove) {
        //TODO
        int sizeDifference = bigComponent.size() - smallComponent.size();

        if (0 < sizeDifference && sizeDifference <= verticesToMove &&
                sizeDifference <= compensator.getCount()) {
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

                return sizeDifference;
            }
        }

        return 0;
    }

    /**
     * Decrease the width by moving on vertex from small3Big to otherColor
     * and one of it's big1Small to smallColor.
     *
     * @param small3Big      vertices from smallColor with all neighbours in bigColor
     * @param compensator    list of vertices that can be used to compensate
     * @param verticesToMove desired change of the coloring width
     * @return how much the width was decreased
     */
    private int moveSoleNeighbours(LinkedList<Integer> small3Big,
                                   BooleanArray compensator, int verticesToMove) {
        int verticesMoved = 0;

        for (Integer vertex : small3Big) {

            for (Integer neighbour: graph.getNeighbours(vertex)) {
                if (verticesToMove < 1) {
                    return verticesMoved;
                }

                if (X3Y.getNeighboursInY(neighbour, colorSmall, coloring) == 1) {
                    coloring.set(vertex, colorOther);
                    coloring.set(neighbour, colorSmall);
                    compensator.set(vertex, false);

                    verticesMoved++;
                    verticesToMove--;

                    break;
                }
            }
        }

        return verticesMoved;
    }

    /**
     * Decrease the width by making swaps involving pairs of
     * vertices from small3Big and it's common neighbourhood.
     *
     * @param small3Big   vertices from smallColor with all neighbours in bigColor
     * @param compensator list of vertices that can be used to compensate
     * @return how much the width was decreased
     */
    private boolean moveCommonNeighbours(int w, LinkedList<Integer> small3Big,
                                         BooleanArray compensator) {
        //TODO
        LinkedList<Integer> potentialWithCommonNeigh = new LinkedList<>(small3Big);
        int[] withCommonNeigh;
        LinkedList<Integer> commonNeigh;
        int x, y, tmp;

        if (w == NO_VERTEX) {
            return false;
        }

        while (!potentialWithCommonNeigh.isEmpty()) {
            withCommonNeigh = findSmall3BigWithCommonNeigh(potentialWithCommonNeigh);
            if (withCommonNeigh == null) {
                break;
            }

            x = withCommonNeigh[0];
            y = withCommonNeigh[1];

            commonNeigh = getCommonNeigh(x, y);

            //TODO rethink and maybe add separate method
            if (commonNeigh.size() == 3) {
                tmp = x;
                x = X3Y.findOneInX3Y(colorOther, colorBig, coloring);
                coloring.set(tmp, colorOther);
                coloring.set(x, colorSmall);
                //TODO changes in components structure and compensator
                //TODO changing common neigh and list of potential vertices
            }

            if (moveCommonAndSpare(x, y, w, commonNeigh, small3Big, compensator)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Find two vertices in small3Big that have some common
     * neighbourhood.
     *
     * @param small3Big vertices from smallColor with all neighbours in bigColor
     * @return array with two vertices with common neighbours or null if not found
     */
    private int[] findSmall3BigWithCommonNeigh(LinkedList<Integer> small3Big) {
        int vertex;

        while (!small3Big.isEmpty()) {
            vertex = small3Big.poll();
            for (Integer neighbour : graph.getNeighbours(vertex)) {
                for (Integer otherVertex : graph.getNeighbours(neighbour)) {
                    if (X3Y.has3NeighboursInY(otherVertex, colorBig, coloring) &&
                            coloring.get(otherVertex) == colorSmall &&
                            otherVertex != vertex) {
                        return new int[]{vertex ,otherVertex};
                    }
                }
            }
        }

        return null;
    }

    /**
     * Decrease the width by making a swap involving 2 vertices
     * from small3Big and it's common neighbourhood.
     *
     * @param x           vertex from small3Big
     * @param y           vertex from small3Big
     * @param w           vertex from other3Big
     * @param commonNeigh list of x and y common neighbours
     * @param small3Big   vertices from smallColor with all neighbours in bigColor
     * @param compensator list of vertices used to compensate
     * @return true if width was decreased
     */
    private boolean moveCommonAndSpare(int x, int y, int w, LinkedList<Integer> commonNeigh,
                                       LinkedList<Integer> small3Big, BooleanArray compensator) {

        for (Integer neighbour : commonNeigh) {
            if (!graph.hasEdge(neighbour, w)) {
                coloring.set(neighbour, colorSmall);
                coloring.set(w, colorSmall);
                coloring.set(x, colorOther);
                coloring.set(y, colorOther);

                compensator.set(x, false);
                compensator.set(y, false);
                //TODO fixing components structure messed up with w
                //TODO consider that w might be added to small3Big

                return true;
            }
        }

        if (2 < small3Big.size()) {
            for (Integer z : small3Big) {
                if (z != x && z != y) {
                    coloring.set(commonNeigh.poll(), colorOther);
                    coloring.set(w, colorSmall);
                    coloring.set(z, colorOther);

                    compensator.set(z, false);
                    //TODO consider that w might be added to small3Big

                    return true;
                }
            }
        }

        return false;
    }

    private int reduceToPathsAndSwap(int w, LinkedList<Integer> bigComponent) {
        //TODO
        BitSet checked = new BitSet(graph.getVertices());
        LinkedList<Integer> currentPath;

        for (Integer i : bigComponent) {
            if (!checked.get(i)) {
                checked.set(i);

                currentPath = findPathOfOddSize(i, checked);

                if (currentPath == null) {
                    continue;
                }

                swapWithinPath(w, currentPath);
                return 1;
            }
        }

        return 0;
    }

    private LinkedList<Integer> findPathOfOddSize(int current, BitSet checked) {
        //TODO
        Queue<Integer> queue = new LinkedList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int currentColor, otherColor;
        ArrayList<Integer> terminalVertices = new ArrayList<>(2);

        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.poll();
            currentColor = coloring.get(current);

            if (!isAllowedVertexInPath(current)) {
                continue;
            }

            if (isTerminalVertex(current)) {
                if (currentColor == colorSmall) {
                    return null;
                }
                terminalVertices.add(current);
            }
            else {
                path.add(current);
            }

            if (currentColor == colorBig) {
                otherColor = colorSmall;
            } else {
                otherColor = colorBig;
            }

            for (Integer neighbour : graph.getNeighbours(current)) {
                if (coloring.get(neighbour) == otherColor && !checked.get(neighbour)) {
                    queue.add(neighbour);
                }
                checked.set(neighbour);
            }
        }

        if (2 < path.size()) {
            if (terminalVertices.size() < 2) {
                int i = 5;
            }
            path.addFirst(terminalVertices.get(0));
            path.addLast(terminalVertices.get(1));

            return path;
        }

        return null;
    }

    private void swapWithinPath(int w, LinkedList<Integer> path) {
        //TODO
        int begin, end, x, y = NO_VERTEX;

        begin = path.getFirst();
        end = path.getLast();

        x = findNeighbourInSmall3Big(begin);

        if (x == NO_VERTEX) {
            x = findNeighbourInSmall3Big(end);
        }
        else {
            y = findNeighbourInSmall3Big(end);
        }

        if (y == NO_VERTEX) {
            swapColorsInPath(path);
            coloring.set(x, colorOther);
        }
        else {
            coloring.set(w, colorSmall);

            int neighbourInPath = NO_VERTEX;

            for (Integer vertex : path) {
                if (graph.hasEdge(vertex, w)) {
                    neighbourInPath = vertex;
                    break;
                }
            }

            if (neighbourInPath == NO_VERTEX) {
                coloring.set(x, colorOther);
                coloring.set(y, colorOther);
                swapColorsInPath(path);
            }
            else {
                coloring.set(neighbourInPath, colorOther);

                if (graph.hasEdge(neighbourInPath, x)) {
                    coloring.set(y, colorOther);
                }
                else {
                    coloring.set(x, colorOther);
                }
            }
        }
    }

    private int findNeighbourInSmall3Big(int vertex) {
        for (Integer neighbour : graph.getNeighbours(vertex)) {
            if (coloring.get(neighbour) == colorSmall &&
                    X3Y.has3NeighboursInY(neighbour, colorBig, coloring)) {
                return neighbour;
            }
        }

        return NO_VERTEX;
    }

    private void swapColorsInPath(LinkedList<Integer> path) {
        for (Integer vertex : path) {
            if (coloring.get(vertex) == colorSmall) {
                coloring.set(vertex, colorBig);
            }
            else {
                coloring.set(vertex, colorSmall);
            }
        }
    }

    private boolean isTerminalVertex(int vertex) {
        if (coloring.get(vertex) == colorSmall && X3Y.getNeighboursInY(vertex, colorBig, coloring) == 1) {
            return true;
        }

        if (coloring.get(vertex) == colorBig) {
            if (X3Y.getNeighboursInY(vertex, colorSmall, coloring) == 1) {
                return true;
            }

            for (Integer neighbour : graph.getNeighbours(vertex)) {
                if (coloring.get(neighbour) == colorSmall &&
                    X3Y.has3NeighboursInY(neighbour, colorBig, coloring)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isAllowedVertexInPath(int vertex) {
        int neighboursInSmall3Big = 0;

        if (coloring.get(vertex) == colorBig) {
            for (Integer neighbour : graph.getNeighbours(vertex)) {
                if (X3Y.has3NeighboursInY(neighbour, colorBig, coloring) &&
                    coloring.get(neighbour) == colorSmall) {
                    neighboursInSmall3Big++;
                }

                if (1 < neighboursInSmall3Big) {
                    break;
                }
            }

            if (1 < neighboursInSmall3Big) {
                return false;
            }
        }

        if (coloring.get(vertex) == colorSmall && X3Y.has3NeighboursInY(vertex, colorBig, coloring)) {
            return false;
        }

        return true;
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

    private void markComponentsAsChecked(int current, BitSet checked) {
        Queue<Integer> queue = new LinkedList<>();

        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.poll();

            for (Integer neighbour : graph.getNeighbours(current)) {
                if (coloring.get(neighbour) == colorOther && !checked.get(neighbour)) {
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
            if (X3Y.has3NeighboursInY(i, toColor, coloring)) {
                big3Small.add(i);
            }
        }

        return big3Small;
    }

    private LinkedList<Integer> getCommonNeigh(int x, int y) {
        LinkedList<Integer> xNeigh = graph.getNeighbours(x), commonNeigh = new LinkedList<>();

        for (Integer neighbour : graph.getNeighbours(y)) {
            if (xNeigh.contains(neighbour)) {
                commonNeigh.add(neighbour);
            }
        }

        return commonNeigh;
    }
}
