package graph.test;

import graph.ListGraph;
import org.junit.Before;

/**
 * Created by Paweł Kopeć on 26.12.16.
 */
public class ListGraphTest extends GraphTest {

    @Before
    public void setGraph() {
        this.graph = new ListGraph(5);
    }
}
