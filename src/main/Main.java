package main;

import graph.ListGraph;

import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        //Tester.runAllTests();
        ListGraph g = new ListGraph(5);
        g.addEdge(3, 4);
        g.addEdge(0, 2);
        g.addEdge(3, 1);
        g.addEdge(2, 3);
        g.addEdge(1, 2);
        g.addEdge(1, 4);
        System.out.println(g);
        g.removeEdge(1, 3);
        System.out.println(g);
        g.makeEmpty();
        System.out.println(g);
    }
}
