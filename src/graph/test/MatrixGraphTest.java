package graph.test;

import graph.GraphFactory;
import graph.MatrixGraph;
import org.junit.Before;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Test for graph implementation based on adjacency matrix.
 */
public class MatrixGraphTest extends GraphTest {
    @Before
    @Override
    public void initGraphSubclass() { graphType = MatrixGraph.class; }
}
