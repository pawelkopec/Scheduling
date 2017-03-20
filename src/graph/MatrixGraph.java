package graph;

import java.io.InputStream;
import java.util.BitSet;
import java.util.LinkedList;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Implementation of graph data structure
 * based on adjacency matrix.
 *
 * TODO
 * 1. Think of data structure for adjacency matrix
 * based on bit operations, not integers.
 * Update - Java BitSet will do.
 * 2. Get rid of redundancies in adjacency matrix
 */
public class MatrixGraph extends BaseGraph {

    private static final boolean CONNECTED = true;
    private static final boolean DISCONNECTED = false;

    private BitSet adjacencyMatrix;
    private int[] neighbourNumbers;

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

    public MatrixGraph(BaseGraph other) {
        super(other);
    }

    @Override
    protected void initContainers(int verticesNumber, int edgesNumber) {
        adjacencyMatrix = new BitSet(verticesNumber*verticesNumber);
        neighbourNumbers = new int[verticesNumber];
    }

    @Override
    public void addEdge(int from, int to) {
        validateEdge(from, to);

        adjacencyMatrix.set(from * verticesNumber + to);
        adjacencyMatrix.set(to * verticesNumber + from);

        neighbourNumbers[from]++;
        neighbourNumbers[to]++;
        edgesNumber++;
    }

    @Override
    public void removeEdge(int from, int to) {
        if(!hasEdge(from, to)) {
            throw new IllegalArgumentException(NO_SUCH_EDGE);
        }
        adjacencyMatrix.clear(from * verticesNumber + to);
        adjacencyMatrix.clear(to * verticesNumber + from);

        neighbourNumbers[from]--;
        neighbourNumbers[to]--;
        edgesNumber--;
    }

    @Override
    public LinkedList<Integer> getNeighbours(int index) {
        validateVertex(index);

        LinkedList<Integer> neighbours = new LinkedList<>();
        for(int i = 0; i < verticesNumber; i++) {
            if(adjacencyMatrix.get(index * verticesNumber + i) ) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    @Override
    public int getNeighboursNumber(int index) {
        validateVertex(index);
        return neighbourNumbers[index];
    }

    @Override
    public boolean hasEdge(int from, int to) {
        validateEdge(from, to);
        return adjacencyMatrix.get(from * verticesNumber + to);
    }

    @Override
    public void makeEmpty() {
        adjacencyMatrix.clear();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("").append(verticesNumber).append(" ");
        str.append(edgesNumber).append(" ");

        for(int i = 0; i < verticesNumber; i++) {
            for(int j = i + 1; j < verticesNumber; j++) {
                if (adjacencyMatrix.get(i * verticesNumber + j)) {
                    str.append(i).append(" ").append(j).append(" ");
                }
            }
        }

        return str.toString();
    }
}