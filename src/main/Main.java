package main;

import graph.RegularGraph;
import graph.RegularListGraph;
import graph.util.BipartiteRegularGraphGenerator;
import scheduling.three.ThreeMachinesScheduler;
import scheduling.three.test.ThreeMachinesSchedulerTest;

public class Main {

    public static void main(String[] args) {
        BipartiteRegularGraphGenerator generator = new BipartiteRegularGraphGenerator();

        RegularGraph exampleGraph = generator.getRandomGraph(RegularListGraph.class, 100000, 3);
        double[] speeds = new double[]{432.3, 321.4, 523.2};
        ThreeMachinesScheduler scheduler = new ThreeMachinesScheduler(exampleGraph, speeds);


        ThreeMachinesSchedulerTest.assertCorrectSchedule(scheduler);
    }
}
