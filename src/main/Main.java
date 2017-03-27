package main;

import graph.Graph;
import graph.RegularGraph;
import graph.RegularListGraph;
import graph.VertexColoring;
import graph.test.ListGraphTest;
import graph.test.MatrixGraphTest;
import graph.test.VertexColoringTest;
import graph.util.RegularGraphGenerator;
import scheduling.three.ThreeMachinesScheduler;
import scheduling.three.test.ThreeMachinesSchedulerTest;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;
import static scheduling.three.Const.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        //runAllTests();
        Scanner scanner = new Scanner(System.in);
        while (scanner.nextLine() != "") {
            RegularGraphGenerator generator = new RegularGraphGenerator();
            RegularGraph graph = generator.getRandomBipartiteGraph(RegularListGraph.class, 3, 3000);
            System.out.println("Wygenerowano");
            assertTrue(Graph.isBiparte(graph));
            System.out.println("Sprawdzono dwudzielność");

            ThreeMachinesScheduler scheduler = new ThreeMachinesScheduler(graph, new double[]{44.5, 64.4, 48.4});
            VertexColoring coloring = scheduler.findScheduling();
            int [] division = scheduler.getDivision();
            System.out.println("Znaleziono kolorowanie");
            assertTrue(coloring.isProper());

            System.out.println("Sprawdzono poprawność kolorowania");
            System.out.println("Podział: " + Arrays.toString(division));
            int [] colors = new int[] {coloring.getNumberOfColored(C), coloring.getNumberOfColored(B), coloring.getNumberOfColored(A)};
            System.out.println("Kolory : " + Arrays.toString(colors));
            if (coloring.getNumberOfColored(A) == division[2])
                System.out.println("Wielkość A poprawna");
            else
                System.out.println("    Wielkość A niepoprawna");

            if (coloring.getNumberOfColored(B) == division[1])
                System.out.println("Wielkość B poprawna");
            else
                System.out.println("    Wielkość B niepoprawna");

            if (coloring.getNumberOfColored(C) == division[0])
                System.out.println("Wielkość C poprawna");
            else
                System.out.println("    Wielkość C niepoprawna");
        }
    }

    public static void runAllTests() {
        Tester tester = new Tester();
        tester.runClassTest(ListGraphTest.class);
        tester.runClassTest(MatrixGraphTest.class);
        tester.runClassTest(VertexColoringTest.class);
        tester.runClassTest(ThreeMachinesSchedulerTest.class);
    }
}
