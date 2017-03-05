package main;

import graph.RegularListGraph;
import graph.VertexColoring;
import graph.test.ListGraphTest;
import graph.test.MatrixGraphTest;
import graph.test.VertexColoringTest;
import scheduling.BicubicScheduling;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        //runAllTests();
        RegularListGraph graph = new RegularListGraph(20, 0);
        double [] speeds = new double[]{23.4, 5.4, 14.2};
        new BicubicScheduling(graph, new VertexColoring(graph), speeds).findColoring();
    }

    public static void runAllTests() {
        Tester tester = new Tester();
        tester.runClassTest(ListGraphTest.class);
        tester.runClassTest(MatrixGraphTest.class);
        tester.runClassTest(VertexColoringTest.class);
    }
}
