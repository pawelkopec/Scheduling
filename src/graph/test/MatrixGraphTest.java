package graph.test;

import org.junit.Before;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Test for graph implementation based on adjacency matrix.
 */
public class MatrixGraphTest extends GraphTest {
    @Before
    public void init() { graphSubclassName = "graph.MatrixGraph"; }
}
