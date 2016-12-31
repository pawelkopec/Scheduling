package graph.test;

import graph.ListGraph;
import graph.VertexColoring;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Paweł Kopeć on 31.12.16.
 *
 * Tests for coloring class.
 */
public class VertexColoringTest {

    @Test
    public void setGetColor() {
        VertexColoring coloring = new VertexColoring(new ListGraph(12));

        coloring.setColor(10, 6);
        coloring.setColor(7, 3);
        coloring.setColor(4, 9);
        coloring.setColor(8, 6);

        assertEquals(coloring.getColor(10), 6);
        assertEquals(coloring.getColor(7), 3);
        assertEquals(coloring.getColor(4), 9);
        assertEquals(coloring.getColor(8), 6);
        assertEquals(coloring.getColor(0), 0);
        assertEquals(coloring.getColor(1), 0);
        assertEquals(coloring.getColor(2), 0);
        assertEquals(coloring.getColor(3), 0);
        assertEquals(coloring.getColor(5), 0);
        assertEquals(coloring.getColor(6), 0);
        assertEquals(coloring.getColor(9), 0);
        assertEquals(coloring.getColor(11), 0);
    }

    @Test
    public void getColors() {
        VertexColoring coloring = new VertexColoring(new ListGraph(7));

        int[] colors = new int[]{3, 6, 8, 1, 2, 0, 0};

        for(int i = 0; i < 5; i++) {
            coloring.setColor(i, colors[i]);
        }

        assertTrue(Arrays.equals(coloring.getColors(), colors));
    }

}