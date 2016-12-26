package graph;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Implementation of graph data structure
 * based on adjacency matrix.
 */
public class MatrixGraph extends Graph {

    int[][] adjacencyMatrix;

    MatrixGraph() {
        super();
    }

    MatrixGraph(int verticesNumber, int edgesNumber) {
        super(verticesNumber, edgesNumber);
    }

    public MatrixGraph(InputStream in) {
        super(in);
    }

    @Override
    protected void read(InputStream in) throws IllegalArgumentException {
        super.read(in);
    }

    @Override
    protected void initContainers(int verticesNumber, int edgesNumber) {

    }

    @Override
    public void addVertex() {

    }

    @Override
    public void removeVertex(int index) throws NoSuchElementException {

    }

    @Override
    public void addEdge(int from, int to) throws IllegalArgumentException {

    }

    @Override
    public void removeEdge(int from, int to) throws IllegalArgumentException {

    }

    @Override
    public LinkedList<Integer> getNeighbours(int index) throws NoSuchElementException {
        return new LinkedList<>();
    }

    @Override
    public boolean hasEdge(int from, int to) throws IllegalArgumentException {
        return false;
    }

    @Override
    public void makeEmpty() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}