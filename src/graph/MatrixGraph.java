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

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;

    private boolean[][] adjacencyMatrix;

    public MatrixGraph() {
        super();
    }

    public MatrixGraph(int verticesNumber) {
        super(verticesNumber);
    }

    public MatrixGraph(InputStream in) {
        super(in);
    }

    public MatrixGraph(String string) {
        super(string);
    }

    @Override
    protected void initContainers(int verticesNumber, int edgesNumber) {
        adjacencyMatrix = new boolean[verticesNumber][verticesNumber];
    }

    @Override
    public void addEdge(int from, int to) {
        validateEdge(from, to);

        adjacencyMatrix[from][to] = CONNECTED;
        adjacencyMatrix[to][from] = CONNECTED;
        edgesNumber++;
    }

    @Override
    public void removeEdge(int from, int to) {
        if(!hasEdge(from, to)) {
            throw new IllegalArgumentException(NO_SUCH_EDGE);
        }
        adjacencyMatrix[from][to] = DISCONNECTED;
        adjacencyMatrix[to][from] = DISCONNECTED;
        edgesNumber--;
    }

    @Override
    public LinkedList<Integer> getNeighbours(int index) {
        validateVertex(index);

        LinkedList<Integer> neighbours = new LinkedList<>();
        for(int i = 0; i < adjacencyMatrix[index].length; i++) {
            if(adjacencyMatrix[index][i] == CONNECTED) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        validateEdge(from, to);
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