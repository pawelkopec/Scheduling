package graph;

import java.io.InputStream;

/**
 * Created by Paweł Kopeć on 19.02.17.
 *
 * RegularGraph implementation based on neighbour lists.
 */
public class RegularListGraph extends ListGraph implements RegularGraph {

    private static final String TOO_MANY_NEIGHBOURS = "Regular graph cannot have vertex of so many neighbours.";
    private static final String CANNOT_BE_REGULAR = "Such degree and vertices number cannot make regular graph";

    private int degree;
    private boolean isRegular;

    public RegularListGraph(int verticesNumber, int degree) {
        super(verticesNumber);
        setDegree(degree);
    }

    public RegularListGraph(InputStream in, int degree) {
        super(in);
    }

    public RegularListGraph(String string, int degree) {
        super(string);
        setDegree(degree);
    }

    public RegularListGraph(BaseGraph other, int degree) {
        super(other);
        setDegree(degree);
    }

    public RegularListGraph(RegularListGraph other) {
        super(other);
        setDegree(other.getDegree());
    }

    @Override
    protected void validateEdge(int from, int to) {
        if(neighbourList.get(from).size() >= degree || neighbourList.get(to).size() >= degree) {
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

    private void setDegree(int degree) {
        if(degree < 0 || verticesNumber <= degree || (verticesNumber * degree) % 2 != 0) {
            throw new IllegalArgumentException(CANNOT_BE_REGULAR);
        }

        this.degree = degree;
    }

    public static RegularGraph getInstance(int verticesNumber, int degree) {
        return new RegularListGraph(verticesNumber, degree);
    }

    public int getDegree() {
        return degree;
    }

    public boolean isRegular() {
        return isRegular;
    }
}
