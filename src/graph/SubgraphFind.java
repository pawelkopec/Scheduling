package graph;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Paweł Kopeć on 23.12.16.
 *
 * SubgraphFind interface enables operating
 * on graph's subgraphs without changing
 * the topology of initial graph.
 */
public interface SubgraphFind {

    /**
     * From now Graph acts as if there was no vertex of given index
     * and no edges between it and its neighbours.
     *
     * @param index of vertex to be disabled
     * @return true if this vertex was not already disabled
     * @throws NoSuchElementException if there is no vertex of such index
     */
    boolean disable(int index) throws NoSuchElementException;

    /**
     * Undoes action performed by the above.
     * @see #disable(int)
     *
     * @param index of vertex to be enabled
     * @return true if this vertex was not already enabled
     * @throws NoSuchElementException if there is no vertex of such index
     */
    boolean enable(int index) throws NoSuchElementException;

    /**
     * Disables all vertices in a graph.
     *
     * @see #disable(int)
     */
    void disableAll();

    /**
     * Enables all vertices in a graph.
     *
     * @see #enable(int)
     */
    void enableAll();

    /**
     * Returns a list of enabled vertices indexes.
     *
     * @return list of enabled vertices indexes
     */
    LinkedList<Integer> getAllEnabled();

    /**
     * Returns a list of disabled vertices indexes.
     *
     * @return list of disabled vertices indexes
     */
    LinkedList<Integer> getAllDisabled();
}
