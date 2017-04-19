package graph;

import java.io.InputStream;
import java.util.Scanner;

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

    //TODO get rid of this horrible solution when i have time
    public RegularListGraph(InputStream in, int degree) {
        if (!validateDegree(degree)) {
            throw new IllegalArgumentException(CANNOT_BE_REGULAR);
        }
        this.degree = degree;
        scan(new Scanner(in));
    }

    //TODO get rid of this horrible solution when i have time
    public RegularListGraph(String string, int degree) {
        if (!validateDegree(degree)) {
            throw new IllegalArgumentException(CANNOT_BE_REGULAR);
        }
        this.degree = degree;
        scan(new Scanner(string));
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
        if(!validateDegreeAndVerticesNumber(verticesNumber, degree)) {
            throw new IllegalArgumentException(CANNOT_BE_REGULAR);
        }

        this.degree = degree;
    }

    @Override
    protected boolean validateVerticesNumber(int verticesNumber) {
        return super.validateVerticesNumber(verticesNumber);
    }

    protected boolean validateDegree(int degree) {
        return 0 < degree;
    }

    private boolean validateDegreeAndVerticesNumber(int verticesNumber, int degree) {
        return  validateDegree(degree) &&
                degree < verticesNumber &&
                (verticesNumber * degree) % 2 == 0;
    }

    public int getDegree() {
        return degree;
    }

    public boolean isRegular() {
        return isRegular;
    }
}
