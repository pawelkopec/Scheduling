package scheduling.test;

import graph.RegularListGraph;
import graph.VertexColoring;
import graph.util.RegularGraphGenerator;
import org.junit.Test;
import scheduling.triple.ThreeMachinesScheduler;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scheduling.triple.Const.*;

/**
 * Created by Paweł Kopeć on 05.03.17.
 *
 * Tests for ThreeMachinesScheduling class.
 */
public class ThreeMachinesSchedulerTest {

    private static final int TEST_NUMBER = 30, MIN_GRAPH_SIZE = 1000, MAX_GRAPH_SIZE = 5000;

    private RegularGraphGenerator generator;
    private Random random;

    public ThreeMachinesSchedulerTest() {
        generator = new RegularGraphGenerator();
        random = new Random();
    }

    @Test
    public void chooseOptimalAlgorithm() {
        RegularListGraph graph;

        for (int i = 0; i < 100; i++) {
            graph = generator.getRandomBipartiteGraph(RegularListGraph.class, 3, 30);
            ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 14.3, 7.43});
            assertEquals(scheduling.getState(), ThreeMachinesScheduler.OPTIMAL);
        }
    }

    @Test
    public void chooseSuboptimalAlgorithm() {
        RegularListGraph graph = tricubic();

        ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 1.43, 1.43});

        assertEquals(scheduling.getState(), ThreeMachinesScheduler.SUBOPTIMAL);
    }

    @Test
    public void chooseBruteForceAlgorithm() {
        RegularListGraph graph = tricubic();

        ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 13.43, 1.43});

        assertEquals(scheduling.getState(), ThreeMachinesScheduler.BRUTE_FORCE);
    }

    @Test
    public void applyBruteForceEasyAlgorithm() {
        RegularListGraph graph = generator.getRandomBipartiteGraph(RegularListGraph.class, 3, 6);

        ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 13.43, 1.43});

        assertEquals(scheduling.getState(), ThreeMachinesScheduler.BRUTE_FORCE_EASY);
    }

    @Test
    public void applyOptimalAlgorithmForOneBigPartition() {
        RegularListGraph graph;
        VertexColoring coloring;

        for (int i = 0; i < TEST_NUMBER; i++) {
            graph = generator.getRandomBipartiteGraph(RegularListGraph.class, 3, randomGraphSize());
            ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 14.3, 4.43});
            assertCorrectSchedule(scheduling, scheduling.findScheduling());
        }
    }

    @Test
    public void applyOptimalAlgorithmForClw() {
        RegularListGraph graph;
        VertexColoring coloring;

        //TODO
    }

    // TODO
    // get example graphs from generator after implementing generating tricubic graphs
    private RegularListGraph tricubic() {
        RegularListGraph graph = new RegularListGraph(14, 3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);
        graph.addEdge(6, 7);
        graph.addEdge(7, 8);
        graph.addEdge(8, 9);
        graph.addEdge(9, 10);
        graph.addEdge(10, 11);
        graph.addEdge(11, 12);
        graph.addEdge(12, 13);
        graph.addEdge(13, 0);
        graph.addEdge(1, 13);
        graph.addEdge(0, 11);
        graph.addEdge(10, 12);
        graph.addEdge(7, 9);
        graph.addEdge(4, 8);
        graph.addEdge(3, 5);
        graph.addEdge(2, 6);

        return graph;
    }

    private void assertCorrectSchedule(ThreeMachinesScheduler scheduler, VertexColoring coloring) {
        int [] division = scheduler.getDivision();
        assertTrue(coloring.isProper());
        assertEquals(coloring.getNumberOfColored(A), division[FASTEST]);
        assertEquals(coloring.getNumberOfColored(B), division[MIDDLE]);
        assertEquals(coloring.getNumberOfColored(C), division[SLOWEST]);

    }

    private int randomGraphSize() {
        return random.nextInt(((MAX_GRAPH_SIZE - MIN_GRAPH_SIZE) / 2)) * 2 + MIN_GRAPH_SIZE;
    }
}