package graph;

import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * Colorable interface enables graph coloring
 * and dividing graph into color classes.
 * Color is referenced to as an int.
 */
public interface Colorable {

    /**
     * Set color of a given vertex.
     *
     * @param index of a vertex to be colored
     * @param color that is to be assigned to a vertex
     * @throws NoSuchElementException if there is no vertex of such index
     */
    void setColor(int index, int color) throws NoSuchElementException;

    /**
     * Get color of a given vertex.
     *
     * @param index of a given vertex
     * @return color of a given vertex
     * @throws NoSuchElementException if there is no vertex of such index
     */
    int getColor(int index) throws  NoSuchElementException;

    /**
     * Get division of a graph into color classes.
     * Indexes of outer array represent given colors.
     * Indexes of inner arrays represent colored vertices.
     *
     * @return array of color classes
     */
    default int[][] getColoredClasses() {
        return new int[0][];
    }

    /**
     * Get array of vertices that have a given color.
     *
     * @param color which we look for
     * @return array of indexes of vertices in goven color
     */
    default int[] getColored(int color) {
        return new int [0];
    }
}
