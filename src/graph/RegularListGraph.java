package graph;

import java.io.InputStream;

/**
 * Created by Paweł Kopeć on 19.02.17.
 *
 * Graph subclass that enables to take control over
 * whether graph is regular.
 */
public class RegularListGraph extends ListGraph {

    private static final String TOO_MANY_NEIGHBOURS = "Regular graph cannot have vertex of so many neighbours.";
    private int degree;
    private boolean isRegular;

    public RegularListGraph(int verticesNumber, int degree) {
        super(verticesNumber);
        this.degree = degree;
    }

    public RegularListGraph(InputStream in, int degree) {
        super(in);
    }

    public RegularListGraph(String string, int degree) {
        super(string);
    }

    private boolean setDegree(int degree) {
        return !((degree < 1) || verticesNumber - 1 < degree);
    }

    @Override
    protected void validateEdge(int from, int to) {
        if(neighbourList.get(from).size() >= 3 || neighbourList.get(to).size() >= 3) {
            throw new IllegalArgumentException(TOO_MANY_NEIGHBOURS);
        }
        super.validateEdge(from, to);
    }

    @Override
    public void addEdge(int from, int to) {
        super.addEdge(from, to);
        if(2 * edgesNumber == 3 * verticesNumber) {
            isRegular = true;
        }
    }

    @Override
    public void removeEdge(int from, int to) {
        super.removeEdge(from, to);
        isRegular = false;
    }

    public int getDegree() {
        return degree;
    }

    public boolean isRegular() {
        return isRegular;
    }
}
