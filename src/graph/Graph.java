package graph;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Abstract class for implementation of graph
 * abstract data structure.
 */
public abstract class Graph {

    Graph() {
        initContainers(0, 0);
    }

    Graph(InputStream in) {
        read(in);
    }

    /**
     * Reads a graph from input stream.
     *
     * @param in stream from which graph is read
     * @throws IllegalArgumentException if input format is incorrect
     */
    protected void read(InputStream in) throws IllegalArgumentException {
        Scanner scanner = new Scanner(in);

        int verticesSize = scanner.nextInt();
        if(verticesSize < 0) {
            throw new IllegalArgumentException("Number of vertices in a graph cannot be negative");
        }

        int edgesNumber = scanner.nextInt();
        if(edgesNumber < 0) {
            throw new IllegalArgumentException("Number of edges in a graph cannot be negative");
        }

        initContainers(verticesSize, edgesNumber);

        int from, to;
        for(int i = 0; i < edgesNumber; i++) {
            from = scanner.nextInt();
            to = scanner.nextInt();
            addEdge(from, to);
        }
    }

    /**
     * Initialize all necessary containers
     * to provide abstraction for further initialization.
     * @see #read(InputStream)
     *
     * @param verticesNumber number of vertices
     * @param edgesNumber number of edges
     */
    protected abstract void initContainers(int verticesNumber, int edgesNumber);

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
     *
     * @param index of vertex to be removed
     * @throws NoSuchElementException if there is no vertex of such index
     */
    public abstract void removeVertex(int index) throws NoSuchElementException;

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
    public abstract int[] getNeighbours(int index) throws NoSuchElementException;

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
