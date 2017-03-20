package scheduling.test;

import graph.RegularListGraph;
import graph.util.RegularGraphGenerator;
import org.junit.Test;
import scheduling.triple.ThreeMachinesScheduler;

import static org.junit.Assert.assertEquals;

/**
 * Created by Paweł Kopeć on 05.03.17.
 *
 * Tests for TricubicScheduling class.
 */
public class ThreeMachinesSchedulerTest {

    private RegularGraphGenerator generator;

    public ThreeMachinesSchedulerTest() {
        generator = new RegularGraphGenerator();
    }

    @Test
    public void chooseOptimalAlgorithm() {
        RegularListGraph graph;

        for (int i = 0; i < 20; i++) {
            graph = generator.getRandomBipartiteGraph(RegularListGraph.class, 3, 30);
            ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 1.43, 1.43});
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
    public void chooseBruteForceEasyAlgorithm() {
        RegularListGraph graph = smallCubic();

        ThreeMachinesScheduler scheduling = new ThreeMachinesScheduler(graph, new double[]{34.6, 13.43, 1.43});

        assertEquals(scheduling.getState(), ThreeMachinesScheduler.BRUTE_FORCE_EASY);
    }

    // TODO
    // get example graphs from generator after implementing generator

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

    private RegularListGraph smallCubic() {
        RegularListGraph graph = new RegularListGraph(6, 3);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);
        graph.addEdge(0, 5);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(1, 5);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);

        return graph;
    }
}