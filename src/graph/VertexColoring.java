package graph;

import graph.Graph;

import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 30.12.16.
 *
 * VertexColoring class provides an interface
 * for assigning colors to graph's vertices
 * and dividing them into color classes.
 */
public class VertexColoring {

    private Graph graph;
    private int verticesNumber;
    private int[] colors;

    public VertexColoring(Graph graph) {
        this.graph = graph;
        verticesNumber = graph.getVerices();
        colors = new int[verticesNumber];
    }

    /**
     * Set color of a given vertex.
     *
     * @param index of a vertex to be colored
     * @param color that is to be assigned to a vertex
     */
    public void setColor(int index, int color) {
        graph.validateVertex(index);
        colors[index] = color;
    }

    /**
     * Get color of a given vertex.
     *
     * @param index of a given vertex
     * @return color of a given vertex
     */
    public int getColor(int index) {
        graph.validateVertex(index);
        return colors[index];
    }

    /**
     * Get array of colors.
     *
     * @return array of colors
     */
    public int[] getColors() {
        return colors;
    }
}
