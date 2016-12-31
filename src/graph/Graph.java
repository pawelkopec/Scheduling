package graph;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Abstract class for implementation of graph
 * abstract data structure.
 */
public abstract class Graph {

    protected int verticesNumber;
    protected int edgesNumber;

    protected static final String INVALID_VERTEX_NUM = "Number of vertices in a graph cannot be negative ";
    protected static final String INVALID_EDGE_NUM = "Number of edges in a graph cannot be negative.";
    public static final String INVALID_VERTEX = "Vertex index out of bounds.";
    protected static final String INVALID_EDGE = "Edge's vertex index out of bounds.";
    protected static final String NO_SUCH_EDGE = "Such edge does not exist.";

    Graph() {
        initContainers(0, 0);
    }

    Graph(int verticesNumber, int edgesNumber) {
        if(verticesNumber < 0) {
            throw new IllegalArgumentException(INVALID_VERTEX_NUM);
        }
        if(edgesNumber < 0) {
            throw new IllegalArgumentException(INVALID_EDGE_NUM);
        }
        this.verticesNumber = verticesNumber;
        this.edgesNumber = edgesNumber;
        initContainers(verticesNumber, edgesNumber);
    }

    public Graph(int verticesNumber) {
        this(verticesNumber, 0);
    }

    Graph(InputStream in) {
        scan(new Scanner(in));
    }

    Graph(String string) {
        scan(new Scanner(string));
    }

    /**
     * Build graph from data provided by a scanner.
     *
     * @param scanner to read data from
     * @throws IllegalArgumentException if graph from given parameters cannot be constructed
     */
    private void scan(Scanner scanner) throws IllegalArgumentException {
        verticesNumber = scanner.nextInt();
        if(verticesNumber < 0) {
            throw new IllegalArgumentException(INVALID_VERTEX_NUM);
        }

        int edgesNumber = scanner.nextInt();
        if(edgesNumber < 0) {
            throw new IllegalArgumentException(INVALID_EDGE_NUM);
        }
        initContainers(verticesNumber, edgesNumber);

        int from, to;
        for(int i = 0; i < edgesNumber; i++) {
            from = scanner.nextInt();
            to = scanner.nextInt();

            validateVertex(from);
            validateVertex(to);

            addEdge(from, to);
        }
    }

    /**
     * Initialize all necessary containers
     * to provide abstraction for further initialization.
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
    public int getVertices() {
        return verticesNumber;
    }

    /**
     * Returns the number of edges.
     *
     * @return the number of edges
     */
    public int getEdges() {
        return edgesNumber;
    }

    /**
     * Adds new edge.
     *
     * @param from index of beginning vertex
     * @param to index of end vertex
     */
    public abstract void addEdge(int from, int to);

    /**
     * Removes an existing edge.
     *
     * @param from index of beginning vertex
     * @param to index of end vertex
     */
    public abstract void removeEdge(int from, int to);

    /**
     * Returns an array of indexes of all
     * neighbours of given vertex.
     *
     * @param index of the vertex whose neighbours are to be found
     * @return array of indexes of vertex's neighbours
     */
    public abstract LinkedList<Integer> getNeighbours(int index);

    /**
     * Checks if there is an edge between vertices
     * of given indexes.
     *
     * @param from index of beginning vertex
     * @param to to index of end vertex
     */
    public abstract boolean hasEdge(int from, int to);

    /**
     * Removes all the edges.
     */
    public abstract void makeEmpty();

    /**
     * Check if this graph can have a vertex such index.
     *
     * @param index of a vertex to be checked
     * @return true if index is valid
     */
    public void  validateVertex(int index) {
        if(!(index < verticesNumber && index >= 0)) {
            throw new IllegalArgumentException(INVALID_VERTEX);
        }
    }

    /**
     * Check if this graph can have an edge between
     * vertices of such indexes.
     *
     * @param from index of edge beginning
     * @param to index of edge end
     * @return if indexes are valid
     */
    protected void validateEdge(int from, int to) {
        try {
            validateVertex(from);
            validateVertex(to);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INVALID_EDGE);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
