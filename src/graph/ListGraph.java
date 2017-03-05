package graph;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Implementation of graph data structure
 * based on adjacency lists.
 */
public class ListGraph extends Graph {

    protected ArrayList<LinkedList<Integer>> neighbourList;

    public ListGraph() {
        super();
    }

    public ListGraph(int verticesNumber) {
        super(verticesNumber, 0);
    }

    public ListGraph(InputStream in) {
        super(in);
    }

    public ListGraph(String string) {
        super(string);
    }

    @Override
    protected void initContainers(int verticesNumber, int edgesNumber) {
        neighbourList = new ArrayList<>(verticesNumber);

        for(int i = 0; i < verticesNumber; i++) {
            neighbourList.add(new LinkedList<>());
        }
    }

    @Override
    public void addEdge(int from, int to) {
        validateEdge(from, to);
        neighbourList.get(from).add(to);
        neighbourList.get(to).add(from);
        edgesNumber++;
    }

    @Override
    public void removeEdge(int from, int to) {
        if(!hasEdge(from, to)) {
            throw new IllegalArgumentException(NO_SUCH_EDGE);
        }
        neighbourList.get(from).removeFirstOccurrence(to);
        neighbourList.get(to).removeFirstOccurrence(from);
        edgesNumber--;
    }

    // TODO
    // change to forEachNeighbourDo()
    @Override
    public LinkedList<Integer> getNeighbours(int index) {
        validateVertex(index);
        return new LinkedList<>(neighbourList.get(index));
    }

    @Override
    public int getNeighboursNumber(int index) {
        validateVertex(index);
        return neighbourList.get(index).size();
    }

    @Override
    public boolean hasEdge(int from, int to) {
        validateEdge(from, to);
        return neighbourList.get(from).contains(to);
    }

    @Override
    public void makeEmpty() {
        neighbourList.forEach(LinkedList::clear);
        edgesNumber = 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("").append(verticesNumber).append(" ");
        str.append(edgesNumber).append(" ");

        int from = 0, to;
        for(LinkedList<Integer> neighbours : neighbourList) {
            for (Integer neighbour : neighbours) {
                to = neighbour;
                if (from < to) {
                    str.append(from).append(" ").append(to).append(" ");
                }
            }
            from++;
        }

        return str.toString();
    }
}
