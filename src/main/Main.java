package main;

import graph.test.ListGraphTest;
import graph.test.MatrixGraphTest;
import graph.test.VertexColoringTest;
import scheduling.test.ThreeMachinesSchedulerTest;

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
        tester.runClassTest(ThreeMachinesSchedulerTest.class);
    }
}
