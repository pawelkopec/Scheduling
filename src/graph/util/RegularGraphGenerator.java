package graph.util;

import graph.RegularGraph;
import graph.RegularGraphFactory;

import java.util.*;

/**
 * Created by Robert Stancel on 04.01.17.
 *
 * This code contains algorithms implementations from
 * A. Steger and N. C. Wormald article available at
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.55.7124&rep=rep1&type=pdf
 */
public class RegularGraphGenerator {
    public enum ALGORITHMS { ALG_1, ALG_2 }

    private static final String INVALID_DEGREE = "Vertex degree cannot be less than 1.";
    private static final String INVALID_VERTICES_NUMBER = "Number of vertices cannot be less than 0.";
    private static final String INVALID_VERTICES_DEGREE = "Number of vertices times degree must be even.";
    private static final String INVALID_DEGREE_CEILING = "Degree must be at least 1 less than number of vertices";

    /**
     * Build random graph with given parameters.
     *
     * @param degree vertices degree
     * @param verticesNumber number of vertices
     * @throws IllegalArgumentException if graph from given parameters cannot be constructed
     */
    public <G extends RegularGraph> G getRandomGraph(Class<G> clazz, int verticesNumber, int degree) throws IllegalArgumentException {
        return getRandomGraph(clazz, verticesNumber, degree, ALGORITHMS.ALG_1);
    }

    /**
     * Build random graph with given parameters.
     *
     * @param degree vertices degree
     * @param verticesNumber number of vertices
     * @param algorithmType which algorithm
     * @throws IllegalArgumentException if graph from given parameters cannot be constructed
     */
    public <G extends RegularGraph> G getRandomGraph(Class<G> clazz,
                                                     int verticesNumber, int degree, ALGORITHMS algorithmType) throws IllegalArgumentException {
        G g;
        if (degree < 1) {
            throw new IllegalArgumentException(INVALID_DEGREE);
        }
        if (verticesNumber < 0) {
            throw new IllegalArgumentException(INVALID_VERTICES_NUMBER);
        }
        if (!(verticesNumber % 2 == 0 || degree % 2 == 0)) {
            throw new IllegalArgumentException(INVALID_VERTICES_DEGREE);
        }
        if (degree > verticesNumber - 1) {
            throw new IllegalArgumentException(INVALID_DEGREE_CEILING);
        }


        switch (algorithmType) {
            case ALG_1:
                g =  getGraphA1(clazz, verticesNumber, degree);
                break;
            case ALG_2:
                g = getGraphA2(clazz, verticesNumber, degree);
                break;
            default:
                return null;
        }

        return g;

    }

    protected <G extends RegularGraph> G getGraphA1(Class<G> clazz, int verticesNumber, int degree) {
        G g;
        do {
            g = RegularGraphFactory.getInstance(clazz, verticesNumber, degree);
            /*
             * Start with nd points {1, 2, ..., nd} (nd even) in verticesNumber groups.
             * Put points = {1, 2, ..., nd} (points denotes the set of unpaired points.)
             */
            Random rand = new Random();
            List<Integer> pairs = new ArrayList<>(verticesNumber * degree +1);
            pairs.add(0);
            List<Integer> points = new LinkedList<>();
            for (int i = 1, nd = verticesNumber * degree; i <= nd; i++) {
                points.add(i);
                pairs.add(0);
            }

            /*
             * Repeat the following until no suitable pair can be found:
             * Choose two random points i and j in points,
             * and if they are suitable, pair i with j and delete i and j from points.
             */
            Integer i, j;
            while (existsSuitable(points, pairs, verticesNumber, degree)) {
                i = getFirstPoint(rand, points, pairs, verticesNumber, degree);
                j = getSecondPoint(rand, points, pairs, verticesNumber, degree);
                if (areSuitable(i, j, pairs, verticesNumber, degree)) {
                    pairs.set(i, j);
                    pairs.set(j, i);
                    points.remove(i);
                    points.remove(j);
                }

            }

            /*
             * Create a graph G with edge from vertex r to vertex s if and only if there is a pair
             * containing points in the r'th and s'th groups. If G is degree-regular, output it, otherwise
             * return to Step 1.
             */
            for (int a = 1, nd = verticesNumber * degree + 1; a < nd; a++) {
                if (pairs.get(a) != 0) {
                    g.addEdge(getPointGroup(pairs.get(a), verticesNumber), getPointGroup(a, verticesNumber));
                    pairs.set(pairs.get(a), 0);
                }
            }
        } while (!g.isRegular(degree));

        return g;
    }

    protected Integer getFirstPoint(Random rand, List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        return points.get(rand.nextInt(points.size()));
    }

    protected Integer getSecondPoint(Random rand, List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        return points.get(rand.nextInt(points.size()));
    }




    protected boolean existsSuitable(List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        if (points.size() == 0) {
            return false;
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (areSuitable(points.get(i), points.get(j), pairs, verticesNumber, degree)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected int getPointGroup(int point, int verticesNumber) {
        return point % verticesNumber;
    }

    protected boolean areSuitable(int a, int b, List<Integer> pairs, int verticesNumber, int degree) {
        /*
         * First, we define two points to be suitable
         * if they lie in different groups and no currently existing pair
         * contains points in the same two groups.
         */
        int aGroup = getPointGroup(a, verticesNumber);
        int bGroup = getPointGroup(b, verticesNumber);

        if (aGroup == bGroup) {
            return false;
        }

        int aCurr, bCurr, aCurrPair, bCurrPair;
        for (int i = 0; i < degree; i++) {
            aCurr = aGroup + i * verticesNumber;
            bCurr = bGroup + i * verticesNumber;
            aCurrPair = pairs.get(aCurr);
            bCurrPair = pairs.get(bCurr);

            if ((aCurrPair != 0 && getPointGroup(aCurrPair, verticesNumber) == bGroup) ||
                    (bCurrPair != 0 && getPointGroup(bCurrPair, verticesNumber) == aGroup)) {
                return false;
            }
        }

        return true;
    }

    private <G extends RegularGraph> G getGraphA2(Class<G> clazz, int verticesNumber, int degree) {
        //TODO

        G g;
        do {
            /*
             * Start with a graph G with n vertices {1, 2, ... , n} and no edges
             */
            g = RegularGraphFactory.getInstance(clazz, verticesNumber);

            /*
             * Repeat the following until the set S is empty: Let S denote the set of pairs of vertices
             * of G which are non-adjacent and both have degree at most d − 1. Choose a random
             * pair {u, v} in S with probability proportional to (d−d(u))(d−d(v)) where d denotes
             * the degree in G. Add the edge {u, v} to G.
             */
            List<List<Integer>> listOflistsOfvertices = new ArrayList<>(degree-1);
            for (int i = 0; i < degree-1; i++) {
                listOflistsOfvertices.add(new LinkedList<>());
            }

            for (int v = 0; v < verticesNumber; v++) {
                listOflistsOfvertices.get(0).add(v);
            }

            List<Integer> degreeProbabilityArray = new ArrayList<>((degree*(degree+1))/2);
            for (int d = 0; d < degree; d++) {
                for (int p = 0; p < degree - d; p++) {
                    degreeProbabilityArray.add(d);
                }
            }

            Random rand = new Random();

            int vDegree, uDegree, v, u;

            do {
                do {
                    vDegree = rand.nextInt(degreeProbabilityArray.size());
                } while (listOflistsOfvertices.get(vDegree).isEmpty());
                v = listOflistsOfvertices.get(vDegree).remove(0);

                do {
                    uDegree = rand.nextInt(degreeProbabilityArray.size());
                } while (listOflistsOfvertices.get(vDegree).isEmpty());
                u = listOflistsOfvertices.get(uDegree).remove(0);
            } while (false);
        } while (!g.isRegular(degree));

        return getGraphA1(clazz, verticesNumber, degree);
    }


}