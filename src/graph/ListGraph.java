package graph;

import sun.awt.image.ImageWatched;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Implementation of graph data structure
 * based on adjacency lists.
 */
public class ListGraph extends Graph {

    private ArrayList<LinkedList<Integer>> neighbourList;

    public ListGraph() {
        super();
    }

    public ListGraph(int verticesNumber) {
        super(verticesNumber, 0);
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
        neighbourList = new ArrayList<>(verticesNumber);

        for(int i = 0; i < verticesNumber; i++) {
            neighbourList.add(new LinkedList<>());
        }
    }

    @Override
    public void addVertex() {
        neighbourList.add(new LinkedList<>());
        verticesNumber++;
    }

    @Override
    public void removeVertex(int index) throws NoSuchElementException {
        if(!isValidVertex(index)) {
            throw new NoSuchElementException(INVALID_VERTEX);
        }
        neighbourList.remove(index);
        for(LinkedList<Integer> neighbours : neighbourList) {
            neighbours.removeFirstOccurrence(index);
        }
        verticesNumber--;
    }

    @Override
    public void addEdge(int from, int to) throws IllegalArgumentException {
        if(!isValidEdge(from, to)) {
            throw new IllegalArgumentException(INVALID_EDGE);
        }
        neighbourList.get(from).add(to);
        neighbourList.get(to).add(from);
        edgesNumber++;
    }

    @Override
    public void removeEdge(int from, int to) throws IllegalArgumentException {
        if(!hasEdge(from, to)) {
            throw new IllegalArgumentException(NO_SUCH_EDGE);
        }
        neighbourList.get(from).removeFirstOccurrence(to);
        neighbourList.get(to).removeFirstOccurrence(from);
        edgesNumber--;
    }

    @Override
    public LinkedList<Integer> getNeighbours(int index) throws NoSuchElementException {
        return new LinkedList<Integer>(neighbourList.get(index));
    }

    @Override
    public boolean hasEdge(int from, int to) throws IllegalArgumentException {
        return neighbourList.get(from).contains(to);
    }

    @Override
    public void makeEmpty() {
        for(LinkedList<Integer> neighbours : neighbourList) {
            neighbours.clear();
        }
        edgesNumber = 0;
    }

    // TODO
    // work on better performance
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("V: " + verticesNumber + "; ");
        str.append("E: " + edgesNumber + ";");

        ArrayList<LinkedList<Integer>> copy = new ArrayList<LinkedList<Integer>>(neighbourList);
        int index;
        for(int i = 0; i < verticesNumber; i++) {
            Iterator<Integer> iterator = copy.get(i).iterator();
            while(iterator.hasNext()) {
                index = iterator.next();
                str.append(" (" + i + ", " + index + ")");
                copy.get(index).removeFirstOccurrence(i);
            }
        }

        return str.toString();
    }
}
