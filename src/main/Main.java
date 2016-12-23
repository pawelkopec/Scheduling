package main;

import graph.ListGraph;

import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Gandzia Yo");
        ListGraph g = new ListGraph(new FileInputStream("GRAPH.txt"));
    }
}
