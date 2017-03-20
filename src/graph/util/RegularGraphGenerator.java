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
    private static final String INVALID_DEGREE_VERTICES = "Number of vertices times degree must be even.";

    private int degree;
    private int verticesNumber;

    /**
     * Build random graph with given parameters.
     *
     * @param degree vertices degree
     * @param verticesNumber number of vertices
     * @throws IllegalArgumentException if graph from given parameters cannot be constructed
     */
    public <G extends RegularGraph> G getRandomGraph(Class<G> clazz, int degree, int verticesNumber) throws IllegalArgumentException {
        return getRandomGraph(clazz, degree, verticesNumber, ALGORITHMS.ALG_1);
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
            int degree, int verticesNumber, ALGORITHMS algorithmType) throws IllegalArgumentException {
        G g;

        if (degree < 1) {
            throw new IllegalArgumentException(INVALID_DEGREE);
        }
        if (verticesNumber < 0) {
            throw new IllegalArgumentException(INVALID_VERTICES_NUMBER);
        }
        if (!(verticesNumber % 2 == 0 || degree % 2 == 0)) {
            throw new IllegalArgumentException(INVALID_DEGREE_VERTICES);
        }

        this.degree = degree;
        this.verticesNumber = verticesNumber;

        switch (algorithmType) {
            case ALG_1:
                g =  this.getGraphA1(clazz);
                break;
            case ALG_2:
                g = this.getGraphA2(clazz);
                break;
            default:
                return null;
        }

        return g;

    }

    /**
     * Build random bipartite graph with given parameters.
     *
     * @param degree vertices degree
     * @param verticesNumber number of vertices
     * @throws IllegalArgumentException if graph from given parameters cannot be constructed
     */
    public <G extends RegularGraph> G  getRandomBipartiteGraph(Class<G> clazz, int degree, int verticesNumber) throws IllegalArgumentException {
        G g;

        if (degree < 1) {
            throw new IllegalArgumentException(INVALID_DEGREE);
        }
        if (verticesNumber < 0) {
            throw new IllegalArgumentException(INVALID_VERTICES_NUMBER);
        }
        if (!(verticesNumber % 2 == 0 || degree % 2 == 0)) {
            throw new IllegalArgumentException(INVALID_DEGREE_VERTICES);
        }

        this.degree = degree;
        this.verticesNumber = verticesNumber;

        g = this.getBipartiteGraph(clazz);

        return g;
    }

    private <G extends RegularGraph> G getBipartiteGraph(Class<G> clazz) {
        G g;
        do {
            g = RegularGraphFactory.getInstance(clazz, verticesNumber, degree);;
            /*
             * Start with nd points {1, 2, ..., nd} (nd even) in verticesNumber groups.
             * Put points = {1, 2, ..., nd} (points denotes the set of unpaired points.)
             */
            Random rand = new Random();
            List<Integer> pairs = new ArrayList<>(verticesNumber * degree +1);
            List<Integer> points = new LinkedList<>();
            List<Integer> groupsPartitions = new ArrayList<>(verticesNumber+1);
            pairs.add(0);
            groupsPartitions.add(-1);
            for (int i = 1, nd = verticesNumber * degree; i <= nd; i++) {
                points.add(i);
                pairs.add(0);
            }
            for (int i = 1; i <= verticesNumber; i++) {
                groupsPartitions.add(-1);
            }


            /*
             * Repeat the following until no suitable pair can be found:
             * Choose two random points i and j in points,
             * and if they are suitable, pair i with j and delete i and j from points.
             */
            int i, j, iIndex, jIndex, iGroup, jGroup;
            while (existsBipartiteSuitable(points, pairs, groupsPartitions)) {
                iIndex = rand.nextInt(points.size());
                jIndex = rand.nextInt(points.size());
                i = points.get(iIndex);
                j = points.get(jIndex);
                iGroup = getPointGroup(i);
                jGroup = getPointGroup(j);
                if (areBipartiteSuitable(i, j, pairs, groupsPartitions)) {
                    if (groupsPartitions.get(iGroup) == -1 && groupsPartitions.get(jGroup) == -1) {
                        groupsPartitions.set(iGroup, 0);
                        groupsPartitions.set(jGroup, 1);

                    }
                    else if (groupsPartitions.get(iGroup) == -1) {
                        groupsPartitions.set(iGroup, 1 - groupsPartitions.get(jGroup));
                    }
                    else if (groupsPartitions.get(jGroup) == -1) {
                        groupsPartitions.set(jGroup, 1 - groupsPartitions.get(iGroup));
                    }
                    pairs.set(i, j);
                    pairs.set(j, i);
                    if (iIndex > jIndex) {
                        points.remove(iIndex);
                        points.remove(jIndex);
                    }
                    else {
                        points.remove(jIndex);
                        points.remove(iIndex);
                    }
                }
            }

            /*
             * Create a graph G with edge from vertex r to vertex s if and only if there is a pair
             * containing points in the r'th and s'th groups. If G is degree-regular, output it, otherwise
             * return to Step 1.
             */
            for (int a = 1, nd = verticesNumber * degree + 1; a < nd; a++) {
                if (pairs.get(a) != 0) {
                    g.addEdge(getPointGroup(pairs.get(a)), getPointGroup(a));
                    pairs.set(pairs.get(a), 0);
                }
            }
        } while (!g.isRegular());

        return g;
    }

    private boolean existsBipartiteSuitable(List<Integer> points, List<Integer> pairs, List<Integer> groupsPartitions) {
        if (points.size() == 0) {
            return false;
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (areBipartiteSuitable(points.get(i), points.get(j), pairs, groupsPartitions)) {
                    return true;
                }
            }
        }

        return false;
    }


    private <G extends RegularGraph> G getGraphA1(Class<G> graphSubclass) {
        G g = RegularGraphFactory.getInstance(graphSubclass, verticesNumber, degree);
        do {
            /**
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
            int i, j, iIndex, jIndex;
            while (existsSuitable(points, pairs)) {
                iIndex = rand.nextInt(points.size());
                jIndex = rand.nextInt(points.size());
                i = points.get(iIndex);
                j = points.get(jIndex);
                if (areSuitable(i, j, pairs)) {
                    pairs.set(i, j);
                    pairs.set(j, i);
                    if (iIndex > jIndex) {
                        points.remove(iIndex);
                        points.remove(jIndex);
                    }
                    else {
                        points.remove(jIndex);
                        points.remove(iIndex);
                    }
                }
            }

            /**
             * Create a graph G with edge from vertex r to vertex s if and only if there is a pair
             * containing points in the r'th and s'th groups. If G is degree-regular, output it, otherwise
             * return to Step 1.
             */
            for (int a = 1, nd = verticesNumber * degree + 1; a < nd; a++) {
                if (pairs.get(a) != 0) {
                    g.addEdge(getPointGroup(pairs.get(a)), getPointGroup(a));
                    pairs.set(pairs.get(a), 0);
                }
            }
        } while (!g.isRegular());

        return g;
    }

    private boolean existsSuitable(List<Integer> points, List<Integer> pairs) {
        if (points.size() == 0) {
            return false;
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (areSuitable(points.get(i), points.get(j), pairs)) {
                    return true;
                }
            }
        }

        return false;
    }

    private int getPointGroup(int point) {
        return point % verticesNumber;
    }

    private boolean areSuitable(int a, int b, List<Integer> pairs) {
        /*
         * First, we define two points to be suitable
         * if they lie in different groups and no currently existing pair
         * contains points in the same two groups.
         */
        int aGroup = getPointGroup(a);
        int bGroup = getPointGroup(b);

        if (aGroup == bGroup) {
            return false;
        }

        int aCurr, bCurr, aCurrPair, bCurrPair;
        for (int i = 0; i < degree; i++) {
            aCurr = aGroup + i * verticesNumber;
            bCurr = bGroup + i * verticesNumber;
            aCurrPair = pairs.get(aCurr);
            bCurrPair = pairs.get(bCurr);

            if ((aCurrPair != 0 && getPointGroup(aCurrPair) == bGroup) ||
                    (bCurrPair != 0 && getPointGroup(bCurrPair) == aGroup)) {
                return false;
            }
        }

        return true;
    }

    private boolean areBipartiteSuitable(int a, int b, List<Integer> pairs, List<Integer> groupsPartitions) {
        /*
         * First, we define two points to be suitable
         * if they lie in different groups and no currently existing pair
         * contains points in the same two groups.
         */
        int aGroup = getPointGroup(a);
        int bGroup = getPointGroup(b);

        if (aGroup == bGroup) {
            return false;
        }

        if (groupsPartitions.get(aGroup) == groupsPartitions.get(bGroup)
                && (groupsPartitions.get(aGroup) != -1 && groupsPartitions.get(bGroup) != -1)) {
            return false;
        }

        int aCurr, bCurr, aCurrPair, bCurrPair;
        for (int i = 0; i < degree; i++) {
            aCurr = aGroup + i * verticesNumber;
            bCurr = bGroup + i * verticesNumber;
            aCurrPair = pairs.get(aCurr);
            bCurrPair = pairs.get(bCurr);

            if ((aCurrPair != 0 && getPointGroup(aCurrPair) == bGroup) ||
                    (bCurrPair != 0 && getPointGroup(bCurrPair) == aGroup)) {
                return false;
            }
        }

        return true;
    }

    private <G extends RegularGraph> G getGraphA2(Class<G> clazz) {
        //TODO
        return getGraphA1(clazz);
    }
}