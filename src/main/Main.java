package main;

import graph.RegularListGraph;
import graph.VertexColoring;
import graph.test.ListGraphTest;
import graph.test.MatrixGraphTest;
import graph.test.VertexColoringTest;
import scheduling.BicubicScheduling;
import scheduling.TripleScheduling;
import scheduling.test.TricubicSchedulingTest;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        runAllTests();
    }

    public static void runAllTests() {
        Tester tester = new Tester();
        tester.runClassTest(ListGraphTest.class);
        tester.runClassTest(MatrixGraphTest.class);
        tester.runClassTest(VertexColoringTest.class);
        tester.runClassTest(TricubicSchedulingTest.class);
    }
}
