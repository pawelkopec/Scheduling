package scheduling;

import graph.RegularListGraph;
import graph.VertexColoring;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by Paweł Kopeć on 02.03.17.
 *
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines on 2-chromatic cubic graph.
 */
public class BicubicScheduling extends CubicScheduling {

    public BicubicScheduling(RegularListGraph graph, VertexColoring coloring, double[] speeds) {
        super(graph, coloring, speeds);
        System.out.println(speeds);
    }

    public BicubicScheduling(RegularListGraph graph, double[] speeds) {
        super(graph, speeds);
    }

    public VertexColoring findColoring() {
        //TODO
        return coloring;
    }

    /**
     * Check how to round optimal sizes of color classes
     * to integers to obtain minimal processing time
     * for bicubic graph.
     *
     * @return sizes of color sizes
     */
    int[] findOptimalDivision() {
        int n = graph.getVertices();
        double[] nFloat = new double[3];
        for(int i = 0; i < speeds.length; i++) {
            nFloat[i] = n * speeds[i] / sumOfSpeeds;
        }

        double time2Floor = Math.floor(nFloat[1]) / speeds[1];
        double time2Ceil= Math.ceil(nFloat[1]) / speeds[1];
        double time3Floor = Math.floor(nFloat[2]) / speeds[2];
        double time3Ceil= Math.ceil(nFloat[2]) / speeds[2];
        double totalTime = n / sumOfSpeeds;


        double maxTime1 = Math.max(time3Floor, Math.max(time2Ceil, totalTime - time3Floor - time2Ceil));
        double maxTime2 = Math.max(time3Ceil, Math.max(time2Floor, totalTime - time3Ceil - time2Floor));
        double maxTime3 = Math.max(time3Ceil, Math.max(time2Ceil, totalTime - time3Ceil - time2Ceil));

        double minTime = Math.min(maxTime1, Math.min(maxTime2, maxTime3));

        int[] division = new int[3];

        if(minTime == maxTime1) {
            division[2] = (int)Math.floor(nFloat[2]);
            division[1] = (int)Math.ceil(nFloat[1]);
        }
        else if(minTime == maxTime2) {
            division[2] = (int)Math.ceil(nFloat[2]);
            division[1] = (int)Math.floor(nFloat[1]);
        }
        else {
            division[2] = (int)Math.ceil(nFloat[2]);
            division[1] = (int)Math.ceil(nFloat[1]);
        }
        division[0] = n - division[1] - division[2];

        assertEquals(division[0] + division[1] + division[2], n);
        System.out.println(Arrays.toString(division));
        System.out.println(Arrays.toString(speeds));

        return division;
    }
}
