package graph.test;


import graph.Graph;
import graph.GraphFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by Paweł Kopeć on 25.12.16.
 *
 * Tests for graph class.
 */
abstract public class GraphTest {

    protected String graphSubclassName;

    @Before
    abstract public void initGraphSubclass();

    @Test
    public void addRemoveEdge() throws ClassNotFoundException {
        Graph graph = GraphFactory.getInstance(graphSubclassName, 5);
        graph.addEdge(4, 0);
        graph.addEdge(2, 3);
        graph.addEdge(1, 3);
        graph.addEdge(0, 3);

        assertTrue(graph.hasEdge(4, 0));
        assertTrue(graph.hasEdge(2, 3));
        assertTrue(graph.hasEdge(1, 3));
        assertTrue(graph.hasEdge(0, 3));

        assertFalse(graph.hasEdge(0, 1));
        assertFalse(graph.hasEdge(0, 2));
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(1, 4));
        assertFalse(graph.hasEdge(2, 4));
        assertFalse(graph.hasEdge(3, 4));

        assertEquals(graph.getVertices(), 5);
        assertEquals(graph.getEdges(), 4);

        graph.removeEdge(2, 3);
        graph.removeEdge(1, 3);
        graph.removeEdge(0, 3);

        assertTrue(graph.hasEdge(0, 4));

        assertFalse(graph.hasEdge(2, 3));
        assertFalse(graph.hasEdge(1, 3));
        assertFalse(graph.hasEdge(0, 3));
        assertFalse(graph.hasEdge(0, 1));
        assertFalse(graph.hasEdge(0, 2));
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(1, 4));
        assertFalse(graph.hasEdge(2, 4));
        assertFalse(graph.hasEdge(3, 4));

        assertEquals(graph.getVertices(), 5);
        assertEquals(graph.getEdges(), 1);
    }

    @Test
    public void getNeighbours() throws ClassNotFoundException {
        Graph graph = GraphFactory.getInstance(graphSubclassName, 7);
        graph.addEdge(1, 2);
        graph.addEdge(4, 1);
        graph.addEdge(1, 6);
        graph.addEdge(1, 3);

        LinkedList<Integer> neighbours = graph.getNeighbours(1);
        neighbours.sort(Integer::compareTo);
        assertEquals(neighbours, new LinkedList<>(Arrays.asList(2, 3, 4, 6)));
    }

    @Test
    public void read() throws Exception {
        String s = "5 4 0 3 2 3 1 3 4 3";
        Graph graph = GraphFactory.getInstanceFromString(graphSubclassName, s);

        assertTrue(graph.hasEdge(0, 3));
        assertTrue(graph.hasEdge(2, 3));
        assertTrue(graph.hasEdge(1, 3));
        assertTrue(graph.hasEdge(4, 3));

        assertFalse(graph.hasEdge(0, 1));
        assertFalse(graph.hasEdge(0, 2));
        assertFalse(graph.hasEdge(0, 4));
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(1, 4));
        assertFalse(graph.hasEdge(2, 4));


        assertEquals(graph.getVertices(), 5);
        assertEquals(graph.getEdges(), 4);
    }

}