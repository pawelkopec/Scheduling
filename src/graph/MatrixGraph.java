package graph;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Implementation of graph data structure
 * based on adjacency matrix.
 *
 * TODO
 * Think of data structure for adjacency matrix
 * based on bit operations, not integers.
 */
public class MatrixGraph extends Graph {

    private static final int CONNECTED = 1;
    private static final int DISCONNECTED = 0;

    int[][] adjacencyMatrix;

    public MatrixGraph() {
        super();
    }

    public MatrixGraph(int verticesNumber) {
        super(verticesNumber);
    }

    public MatrixGraph(InputStream in) {
        super(in);
    }

    @Override
    protected void initContainers(int verticesNumber, int edgesNumber) {
        adjacencyMatrix = new int[verticesNumber][verticesNumber];
    }

    @Override
    public void addVertex() {

    }

    @Override
    public void removeVertex(int index) throws NoSuchElementException {

    }

    @Override
    public void addEdge(int from, int to) throws IllegalArgumentException {
        if(!isValidEdge(from, to)) {
            throw new IllegalArgumentException(INVALID_EDGE);
        }
        adjacencyMatrix[from][to] = CONNECTED;
        adjacencyMatrix[to][from] = CONNECTED;
        edgesNumber++;
    }

    @Override
    public void removeEdge(int from, int to) throws IllegalArgumentException {
        if(!hasEdge(from, to)) {
            throw new IllegalArgumentException(NO_SUCH_EDGE);
        }
        adjacencyMatrix[from][to] = DISCONNECTED;
        adjacencyMatrix[to][from] = DISCONNECTED;
        edgesNumber--;
    }

    @Override
    public LinkedList<Integer> getNeighbours(int index) throws NoSuchElementException {
        if(!isValidVertex(index)) {
            throw new IllegalArgumentException(INVALID_VERTEX);
        }
        LinkedList<Integer> neighbours = new LinkedList<>();
        for(int i = 0; i < adjacencyMatrix[index].length; i++) {
            if(adjacencyMatrix[index][i] == CONNECTED) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    @Override
    public boolean hasEdge(int from, int to) throws IllegalArgumentException {
        if(!isValidEdge(from, to)) {
            throw new IllegalArgumentException(INVALID_EDGE);
        }
        return adjacencyMatrix[from][to] == CONNECTED;
    }

    @Override
    public void makeEmpty() {
        for(int i = 0; i < verticesNumber; i++) {
            for(int j = 0; j < verticesNumber; j++) {
                adjacencyMatrix[i][j] = DISCONNECTED;
            }
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}