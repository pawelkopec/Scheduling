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

    protected GraphFactory.GRAPH_TYPES graphType;

    @Before
    abstract public void initGraphSubclass();

    @Test
    public void addEdge() {
        Graph graph = GraphFactory.getInstance(graphType, 5);
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
    }

    @Test
    public void removeEdge() {
        Graph graph = GraphFactory.getInstance(graphType, 5);
        graph.addEdge(4, 0);
        graph.addEdge(2, 3);
        graph.addEdge(1, 3);
        graph.addEdge(0, 3);

        graph.removeEdge(2, 3);
        graph.removeEdge(1, 3);
        graph.removeEdge(0, 3);

        assertFalse(graph.hasEdge(2, 3));
        assertFalse(graph.hasEdge(1, 3));
        assertFalse(graph.hasEdge(0, 3));
    }

    @Test
    public void getVertices() {
        Graph graph;

        graph = GraphFactory.getInstance(graphType, 5);
        assertEquals(graph.getVertices(), 5);

        graph = GraphFactory.getInstance(graphType, 432);
        assertEquals(graph.getVertices(), 432);

        graph = GraphFactory.getInstance(graphType, 32);
        assertEquals(graph.getVertices(), 32);

        graph = GraphFactory.getInstance(graphType, 15);
        assertEquals(graph.getVertices(), 15);
    }

    @Test
    public void getEdges() {
        Graph graph = GraphFactory.getInstance(graphType, 20);

        graph.addEdge(3, 4);
        graph.addEdge(12, 14);
        graph.addEdge(0, 2);
        graph.addEdge(11, 7);
        graph.addEdge(5, 9);
        graph.addEdge(17, 12);
        graph.addEdge(14, 10);
        graph.addEdge(3, 9);
        graph.addEdge(2, 8);
        graph.addEdge(10, 19);
        graph.addEdge(19, 3);

        assertEquals(graph.getEdges(), 11);
    }

    @Test
    public void getNeighbours() {
        Graph graph = GraphFactory.getInstance(graphType, 7);
        graph.addEdge(1, 2);
        graph.addEdge(4, 1);
        graph.addEdge(1, 6);
        graph.addEdge(1, 3);

        LinkedList<Integer> neighbours = graph.getNeighbours(1);
        neighbours.sort(Integer::compareTo);
        assertEquals(neighbours, new LinkedList<>(Arrays.asList(2, 3, 4, 6)));
    }

    @Test
    public void read() {
        String s = "5 4 0 3 2 3 1 3 4 3";
        Graph graph = GraphFactory.getInstanceFromString(graphType, s);

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