package graph;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Implementation of graph data structure
 * based on adjacency lists.
 */
public class ListGraph extends Graph {

    private LinkedList<Integer>[] neighbourList;

    public ListGraph() {
        super();
    }

    public ListGraph(InputStream in) {
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
    public int getVerices() {
        return 0;
    }

    @Override
    public int getEdges() {
        return 0;
    }

    @Override
    public void addVertex() {

    }

    @Override
    public void removeVertex(int index) throws NoSuchElementException {

    }

    @Override
    public boolean disableVertex(int index) throws NoSuchElementException {
        return false;
    }

    @Override
    public boolean enableVertex(int index) throws NoSuchElementException {
        return false;
    }

    @Override
    public boolean addEdge(int from, int to) throws IllegalArgumentException {
        return false;
    }

    @Override
    public void removeEdge(int from, int to) throws IllegalArgumentException {

    }

    @Override
    public int[] getNeighbours(int index) throws NoSuchElementException {
        return new int[0];
    }

    @Override
    public int hasEdge(int from, int to) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
