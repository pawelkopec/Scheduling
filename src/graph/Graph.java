package graph;

import java.util.LinkedList;

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

}
