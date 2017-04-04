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
    public void setGet() {
        VertexColoring coloring = new VertexColoring(new ListGraph(12));

        coloring.set(10, 6);
        coloring.set(7, 3);
        coloring.set(4, 9);
        coloring.set(8, 6);

        assertEquals(coloring.get(10), 6);
        assertEquals(coloring.get(7), 3);
        assertEquals(coloring.get(4), 9);
        assertEquals(coloring.get(8), 6);
        assertEquals(coloring.get(0), 0);
        assertEquals(coloring.get(1), 0);
        assertEquals(coloring.get(2), 0);
        assertEquals(coloring.get(3), 0);
        assertEquals(coloring.get(5), 0);
        assertEquals(coloring.get(6), 0);
        assertEquals(coloring.get(9), 0);
        assertEquals(coloring.get(11), 0);
        assertEquals(coloring.getNumberOfColored(3), 1);
        assertEquals(coloring.getNumberOfColored(9), 1);
        assertEquals(coloring.getNumberOfColored(6), 2);
        assertEquals(coloring.getNumberOfColored(0), 8);
        assertEquals(coloring.getColorsNumber(), 4);
    }

    @Test
    public void gets() {
        VertexColoring coloring = new VertexColoring(new ListGraph(7));

        int[] colors = new int[]{3, 6, 8, 1, 2, 0, 0};

        for(int i = 0; i < 5; i++) {
            coloring.set(i, colors[i]);
        }

        assertTrue(Arrays.equals(coloring.getColors(), colors));
    }

}