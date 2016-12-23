package graph;

import java.io.InputStream;
import java.util.NoSuchElementException;

/**
 * Created by vivace on 23.12.16.
 */
public abstract class Graph {

    Graph() {};

    Graph(InputStream in) {

    }

    /**
     * Returns the number of vertices.
     *
     * @return number of vertices
     */
    public abstract int getVerices();

    /**
     * Returns the number of edges.
     *
     * @return the number of edges
     */
    public abstract int getEdges();

    /**
     * Adds new vertex with index = |V|.
     */
    public abstract void addVertex();

    /**
     * Removes the vertex of given index.
     * Warning - this can change indexes of other vertices.
     * @see #disableVertex(int) and
     * @see #enableVertex(int) instead
     *
     * @param index of vertex to be removed
     * @throws NoSuchElementException if there is no vertex of such index
     */
    public abstract void removeVertex(int index) throws NoSuchElementException;

    /**
     * From now Graph acts as if there was no vertex of given index
     * and no edges between it and its neighbours.
     *
     * @param index of vertex to be disabled
     * @return true if this vertex was not already disabled
     * @throws NoSuchElementException if there is no vertex of such index
     */
    public abstract boolean disableVertex(int index) throws NoSuchElementException;

    /**
     * Undos action performed by the above.
     * @see #disableVertex(int)
     *
     * @param index of vertex to be enabled
     * @return true if this vertex was not already enabled
     * @throws NoSuchElementException if there is no vertex of such index
     */
    public abstract boolean enableVertex(int index) throws NoSuchElementException;

    /**
     * Adds new edge.
     *
     * @param from index of beginning vertex
     * @param to index of end vertex
     * @return true if such edge does not
     * @throws IllegalArgumentException if one of given vertices does not exist
     */
    public abstract boolean addEdge(int from, int to) throws IllegalArgumentException;

    /**
     * Removes an existing edge.
     *
     * @param from index of beginning vertex
     * @param to index of end vertex
     * @throws IllegalArgumentException if one of given vertices does not exist
     */
    public abstract void removeEdge(int from, int to) throws IllegalArgumentException;

    /**
     * Returns an array of indexes of all
     * neighbours of given vertex.
     *
     * @param index of the vertex whose neighbours are to be found
     * @return array of indexes of vertex's neighbours
     * @throws NoSuchElementException if there is no vertex of such index
     */
    public abstract int [] getNeighbours(int index) throws NoSuchElementException;

    /**
     * Checks if there is an edge between vertices
     * of given indexes.
     *
     * @param from index of beginning vertex
     * @param to to index of end vertex
     * @return true if such edge exists
     * @throws IllegalArgumentException if one of given vertices does not exist
     */
    public abstract int hasEdge(int from, int to) throws IllegalArgumentException;

    @Override
    public String toString() {
        return super.toString();
    }
}
