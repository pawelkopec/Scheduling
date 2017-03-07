package main;

import graph.RegularListGraph;
import graph.test.ListGraphTest;
import graph.test.MatrixGraphTest;
import graph.test.VertexColoringTest;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        //runAllTests();
        RegularListGraph g = new RegularListGraph(5, 3);
    }

    public static void runAllTests() {
        Tester tester = new Tester();
        tester.runClassTest(ListGraphTest.class);
        tester.runClassTest(MatrixGraphTest.class);
        tester.runClassTest(VertexColoringTest.class);
    }
}
