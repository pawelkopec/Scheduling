package main;

import graph.Graph;
import graph.ListGraph;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        //Tester.runAllTests();
        String s = "7 6 3 4 0 2 3 1 2 3 1 2 1 4";
        Graph g = new ListGraph(s); //GraphFactory.getInstance("graph.ListGraph", 7);
        System.out.println(g);
        g.removeEdge(1, 3);
        System.out.println(g);
        g.makeEmpty();
    }
}
