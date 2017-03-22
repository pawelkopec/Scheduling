package graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Paweł Kopeć on 17.03.17.
 *
 * Interface for implementation of graph
 * abstract data structure.
 */
public interface Graph {
    int getVertices();

    /**
     * Returns the number of edges.
     *
     * @return the number of edges
     */
    int getEdges();

    /**
     * Adds new edge.
     *
     * @param from index of beginning vertex
     * @param to index of end vertex
     */
    void addEdge(int from, int to);

    /**
     * Removes an existing edge.
     *
     * @param from index of beginning vertex
     * @param to index of end vertex
     */
    void removeEdge(int from, int to);

    /**
     * Returns an array of indexes of all
     * neighbours of given vertex.
     *
     * @param index of the vertex whose neighbours are to be found
     * @return array of indexes of vertex's neighbours
     */
    LinkedList<Integer> getNeighbours(int index);

    /**
     * Returns number of vertex with given index.
     *
     * @param index of the vertex whose neighbours are to be counted
     * @return number of neighbours
     */
    int getNeighboursNumber(int index);

    /**
     * Checks if there is an edge between vertices
     * of given indexes.
     *
     * @param from index of beginning vertex
     * @param to to index of end vertex
     */
    boolean hasEdge(int from, int to);

    /**
     * Removes all the edges.
     */
    void makeEmpty();

    /**
     * Check if this graph can have a vertex such index.
     *
     * @param index of a vertex to be checked
     */
    void validateVertex(int index);

    /**
     * Check if graph is 2-chromatic via BFS.
     * Assign colors of color1 and color2 if it is true.
     *
     * @return true if graph is 2-chromatic
     */
    static boolean isBiparte(Graph graph, VertexColoring coloring, int color1, int color2) {
        Queue<Integer> queue = new LinkedList<>();
        int current = 0, currentColor = color1, otherColor, tempColor;

        queue.add(current);
        coloring.set(current, currentColor);
        while (!queue.isEmpty()) {
            current = queue.poll();
            currentColor = coloring.get(current);
            otherColor = currentColor == color1 ? color2 : color1;

            for (int neighbour : graph.getNeighbours(current)) {
                tempColor = coloring.get(neighbour);

                if (tempColor == VertexColoring.NO_COLOR) {
                    coloring.set(neighbour, otherColor);
                    queue.add(neighbour);
                }
                else if (tempColor == currentColor) {
                    return false;
                }
            }
        }

        return true;
    }

    static boolean isBiparte(Graph graph, int color1, int color2) {
        return isBiparte(graph, new VertexColoring(graph), 1, 2);
    }

    static boolean isBiparte(Graph graph, VertexColoring coloring) {
        return isBiparte(graph, coloring, 1, 2);
    }

    static boolean isBiparte(Graph graph) {
        return isBiparte(graph, new VertexColoring(graph));
    }
}
