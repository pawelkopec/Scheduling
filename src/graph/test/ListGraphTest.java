package graph.test;


import graph.ListGraph;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Paweł Kopeć on 25.12.16.
 *
 * Tests for ListGraph class.
 */
public class ListGraphTest {
    @Test
    public void read() throws Exception {

    }

    @Test
    public void addVertex() throws Exception {

    }

    @Test
    public void removeVertex() throws Exception {

    }

    @Test
    public void addRemoveEdge() throws Exception {
        ListGraph listGraph = new ListGraph(5);
        listGraph.addEdge(4, 0);
        listGraph.addEdge(2, 3);
        listGraph.addEdge(1, 3);
        listGraph.addEdge(0, 3);

        assertTrue(listGraph.hasEdge(0, 4));
        assertTrue(listGraph.hasEdge(2, 3));
        assertTrue(listGraph.hasEdge(1, 3));
        assertTrue(listGraph.hasEdge(0, 3));

        assertFalse(listGraph.hasEdge(0, 1));
        assertFalse(listGraph.hasEdge(0, 2));
        assertFalse(listGraph.hasEdge(1, 2));
        assertFalse(listGraph.hasEdge(1, 4));
        assertFalse(listGraph.hasEdge(2, 4));
        assertFalse(listGraph.hasEdge(3, 4));

        assertEquals(listGraph.getVerices(), 5);
        assertEquals(listGraph.getEdges(), 4);

        listGraph.removeEdge(2, 3);
        listGraph.removeEdge(1, 3);
        listGraph.removeEdge(0, 3);

        assertTrue(listGraph.hasEdge(0, 4));

        assertFalse(listGraph.hasEdge(2, 3));
        assertFalse(listGraph.hasEdge(1, 3));
        assertFalse(listGraph.hasEdge(0, 3));
        assertFalse(listGraph.hasEdge(0, 1));
        assertFalse(listGraph.hasEdge(0, 2));
        assertFalse(listGraph.hasEdge(1, 2));
        assertFalse(listGraph.hasEdge(1, 4));
        assertFalse(listGraph.hasEdge(2, 4));
        assertFalse(listGraph.hasEdge(3, 4));

        assertEquals(listGraph.getVerices(), 5);
        assertEquals(listGraph.getEdges(), 1);
    }

    @Test
    public void getNeighbours() throws Exception {

    }

}