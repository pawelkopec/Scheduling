package main;

import graph.Graph;
import graph.GraphFactory;
import graph.ListGraph;

import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        Tester.runAllTests();
        /*Graph g = GraphFactory.getInstance("graph.ListGraph", 7);
        g.addEdge(3, 4);
        g.addEdge(0, 2);
        g.addEdge(3, 1);
        g.addEdge(2, 3);
        g.addEdge(1, 2);
        g.addEdge(1, 4);
        System.out.println(g);
        g.removeEdge(1, 3);
        System.out.println(g);
        g.makeEmpty();*/
    }
}
