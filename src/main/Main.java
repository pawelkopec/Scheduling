package main;

import graph.ListGraph;

import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        ListGraph g = new ListGraph(new FileInputStream("GRAPH.txt"));
        System.out.println(g);
    }
}
