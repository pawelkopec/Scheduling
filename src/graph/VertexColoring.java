package graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Paweł Kopeć on 30.12.16.
 *
 * VertexColoring class provides an interface
 * for assigning colors to graphBase's vertices
 * and dividing them into color classes.
 */
public class VertexColoring extends VertexAssignment {

    public static int NO_COLOR = 0;

    private int[] colors;
    private HashMap<Integer, Integer> colorSizes;

    public VertexColoring(Graph graph) {
        super(graph);
        colors = new int[graph.getVertices()];
        colorSizes = new HashMap<>();
        colorSizes.put(0, graph.getVertices());
    }

    public VertexColoring(Graph graph, String s) {
        this(graph);
        scan(s);
    }

    private void scan(String s) {
        Scanner scanner = new Scanner(s);

        for (int i = 0; i < graph.getVertices(); i++) {
            set(i, scanner.nextInt());
        }
    }

    /**
     * Set color of a given vertex.
     *
     * @param index of a vertex to be colored
     * @param color that is to be assigned to a vertex
     */
    public void set(int index, int color) {
        graph.validateVertex(index);

        colorSizes.put(colors[index], colorSizes.get(colors[index]) - 1);
        colors[index] = color;

        if(colorSizes.get(color) == null) {
            colorSizes.put(color, 1);
        }
        else {
            colorSizes.put(color, colorSizes.get(color) + 1);
        }
    }

    /**
     * Get color of a given vertex.
     * Equals zero by default.
     *
     * @param index of a given vertex
     * @return color of a given vertex
     */
    public int get(int index) {
        graph.validateVertex(index);
        return colors[index];
    }

    /**
     * Get number of colors in this graphBase.
     *
     * @return number of colors
     */
    public int getColorsNumber() {
        return colorSizes.size();
    }

    /**
     * Get number of vertices that are assigned given color.
     *
     * @param color of vertices to look for
     * @return number of vertices in this color
     */
    public int getNumberOfColored(int color) {
        return colorSizes.containsKey(color) ? colorSizes.get(color) : 0;
    }

    public boolean isProper() {
        for (int i = 0; i < graph.getVertices(); i++) {
            for (Integer neighbour : graph.getNeighbours(i)) {
                if (colors[i] == colors[neighbour]) {
                    return false;
                }
            }
        }

        return true;
    }

    public int[] getColors() {
        return colors.clone();
    }

    public HashMap<Integer, Integer> getColorSizes() {
        return new HashMap<>(colorSizes);
    }

    public Graph getGraph() {
        return graph;
    }

    public String toString() {
        return Arrays.toString(colors);
    }
}
