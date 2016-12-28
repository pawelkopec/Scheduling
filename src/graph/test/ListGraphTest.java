package graph.test;

import org.junit.Before;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Test for graph implementation based on neighbour list.
 */
public class ListGraphTest extends GraphTest {
    @Before
    @Override
    public void initGraphSubclass() {
        graphSubclassName = "graph.ListGraph";
    }
}
