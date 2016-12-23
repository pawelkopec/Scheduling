package graph;

import java.io.InputStream;
import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Implementation of graph data structure
 * based on adjacency lists.
 */
public class ListGraph extends Graph {

    ListGraph() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    ListGraph(InputStream in) {
        super(in);
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
