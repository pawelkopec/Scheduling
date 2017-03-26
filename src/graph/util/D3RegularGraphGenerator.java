package graph.util;

import graph.Graph;
import graph.RegularGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Robert Stancel on 07.03.17.
 */
public class D3RegularGraphGenerator extends RegularGraphGenerator {

    /**
     *
     * @param verticesNumber number of vertices
     * @param chromaticNumber chromatic number (if not 2 or 3 then random)
     * @return graph with given parameters
     * @throws IllegalArgumentException if graph from given parameters cannot be constructed
     */
    public <G extends RegularGraph> G getRandomGraph(Class<G> clazz, int verticesNumber, int chromaticNumber) throws IllegalArgumentException {
        G g;

        switch (chromaticNumber) {
            case 2:
                return getRandomBipartiteGraph(clazz, 3, verticesNumber);
            case 3:
                g = getRandomGraph(clazz, 3, verticesNumber);
                LinkedList<LinkedList<Integer>> partitions  = new LinkedList<>();
                partitions.push(new LinkedList<>());
                partitions.push(new LinkedList<>());
                if (isBipartite(g, partitions)) {
                    makeTripartite(g, partitions);
                }
                return g;
            default:
                return getRandomGraph(clazz, 3, verticesNumber);
        }
    }

    private boolean isBipartite(Graph g, LinkedList<LinkedList<Integer>> partitions) {
        int[] verticesColors = new int[g.getVertices()];
        for (int i = 0; i < g.getVertices(); i++) {
            verticesColors[i] = -1;
        }

        verticesColors[0] = 1;
        partitions.get(verticesColors[0]).push(0);

        LinkedList<Integer> queue = new LinkedList<>();
        queue.push(0);
        int u;
        while (!queue.isEmpty()) {
            u = queue.pop();
            for (int v: g.getNeighbours(u)) {
                if (verticesColors[v] ==  -1) {
                    verticesColors[v] = 1 - verticesColors[u];
                    partitions.get(verticesColors[v]).push(v);
                    queue.push(v);
                }
                else if (verticesColors[v] == verticesColors[u]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void makeTripartite(Graph g, LinkedList<LinkedList<Integer>> partitions) {
        int leftA;
        int leftB;
        int rightA;
        int rightB;

        Random rand = new Random();

        do {
            leftA = partitions.get(0).pop();
            leftB = partitions.get(0).pop();
            rightA = g.getNeighbours(leftA).getFirst();
            rightB = g.getNeighbours(leftB).getFirst();

            g.removeEdge(leftA, rightA);
            g.removeEdge(leftB, rightB);
            g.addEdge(leftA, leftB);
            g.addEdge(rightA, rightB);

            partitions.get(1).remove(rightA);
            partitions.get(1).remove(rightB);
        } while (partitions.get(0).size() >= 2 && partitions.get(1).size() >= 2 && rand.nextInt() < 70);

    }

}
