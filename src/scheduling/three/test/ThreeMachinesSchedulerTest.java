package scheduling.three.test;

import graph.RegularListGraph;
import graph.VertexColoring;
import graph.util.BipartiteRegularGraphGenerator;
import org.junit.Test;
import scheduling.three.Const;
import scheduling.three.ThreeMachinesScheduler;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Paweł Kopeć on 05.03.17.
 *
 * Tests for ThreeMachinesScheduling class.
 */
public class ThreeMachinesSchedulerTest {

    private static final int TEST_NUMBER = 300, MIN_GRAPH_SIZE = 1000, MAX_GRAPH_SIZE = 5000;

    private BipartiteRegularGraphGenerator bicubicGenerator;
    private Random random;

    public ThreeMachinesSchedulerTest() {
        bicubicGenerator = new BipartiteRegularGraphGenerator();
        random = new Random();
    }

    @Test
    public void chooseOptimalAlgorithm() {
        RegularListGraph graph;

        for (int i = 0; i < TEST_NUMBER; i++) {
            graph = bicubicGenerator.getRandomGraph(RegularListGraph.class, 300, 3);
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
        RegularListGraph graph = bicubicGenerator.getRandomGraph(RegularListGraph.class, 6, 3);

        ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 13.43, 1.43});

        assertEquals(scheduling.getState(), ThreeMachinesScheduler.BRUTE_FORCE_EASY);
    }

    @Test
    public void applyOptimalAlgorithmForOneBigPartition() {
        RegularListGraph graph;

        for (int i = 0; i < TEST_NUMBER; i++) {
            graph = bicubicGenerator.getRandomGraph(RegularListGraph.class, randomGraphSize(), 3);
            ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 14.3, 4.43});
            assertCorrectSchedule(scheduling.getDivision(), scheduling.findScheduling());
        }
    }

    @Test
    public void applyOptimalAlgorithmForClw() {
        RegularListGraph graph;

        for (int i = 0; i < TEST_NUMBER; i++) {
            graph = bicubicGenerator.getRandomGraph(RegularListGraph.class, randomGraphSize(), 3);
            ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{334.6, 314.3, 301.43});
            assertCorrectSchedule(scheduling.getDivision(), scheduling.findScheduling());
        }
    }

    @Test
    public void applyOptimalAlgorithmForClwForSwappingWhenABComponentsCannotBeSwapped() {
        RegularListGraph graph = new RegularListGraph("10 15 0 5 0 1 0 7 1 8 1 2 2 5 2 9 3 8 3 4 3 6 4 5 4 7 6 9 6 7 8 9", 3);

        ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{334.6, 314.3, 301.43});
        assertCorrectSchedule(scheduling.getDivision(), scheduling.findScheduling());
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

    public static void assertCorrectSchedule(int [] division, VertexColoring coloring) {
        assertTrue(coloring.isProper());
        assertEquals(coloring.getNumberOfColored(Const.A), division[Const.FASTEST]);
        assertEquals(coloring.getNumberOfColored(Const.B), division[Const.MIDDLE]);
        assertEquals(coloring.getNumberOfColored(Const.C), division[Const.SLOWEST]);

    }

    private int randomGraphSize() {
        return random.nextInt(((MAX_GRAPH_SIZE - MIN_GRAPH_SIZE) / 2)) * 2 + MIN_GRAPH_SIZE;
    }
}