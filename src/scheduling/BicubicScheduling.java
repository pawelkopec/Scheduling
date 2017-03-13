package scheduling;

import graph.RegularListGraph;
import graph.VertexColoring;

import static scheduling.Const.B;
import static scheduling.Const.C;

/**
 * Created by Paweł Kopeć on 02.03.17.
 * <p>
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines on 2-chromatic cubic graph.
 */
public class BicubicScheduling extends CubicScheduling {

    /**
     * Speed indexes in array of speeds.
     */
    private static final int FASTEST = 2, MIDDLE = 1, SLOWEST = 0;

    public BicubicScheduling(RegularListGraph graph, VertexColoring coloring, double[] speeds) {
        super(graph, coloring, speeds);
        System.out.println(speeds);
    }

    public BicubicScheduling(RegularListGraph graph, double[] speeds) {
        super(graph, speeds);
    }

    public VertexColoring findColoring() {
        if (speeds[FASTEST] < speeds[SLOWEST] + speeds[MIDDLE]) {
            //TODO check sheet

            int[] division = getOptimalDivision();
            splitBetweenSlowerMachines(division[SLOWEST]);

            ClwWithConstantB clw = new ClwWithConstantB(graph, coloring);

            int toDecrease = division[SLOWEST] - (graph.getVertices() / 2 - division[MIDDLE]);
            clw.decreaseBy(toDecrease);
        } else {
            /*
             * Determine how to split one color class of size n / 2
             * evenly between two slower machines.
             */
            int sizeOfB = (int) Math.ceil((graph.getVertices() / 2) * speeds[MIDDLE] / (speeds[MIDDLE] + speeds[SLOWEST]));
            int sizeOfC = (graph.getVertices() / 2) - sizeOfB;

            double maxTime1 = Math.max(sizeOfB * speeds[MIDDLE], sizeOfC * speeds[SLOWEST]);
            double maxTime2 = Math.max((sizeOfB - 1) * speeds[MIDDLE], (sizeOfC + 1) * speeds[SLOWEST]);

            if (maxTime1 < maxTime2) {
                sizeOfB--;
                sizeOfC++;
            }

            splitBetweenSlowerMachines(sizeOfC);
        }

        return coloring;
    }

    /**
     * Check how to round up optimal sizes of color classes
     * to integers to obtain minimal processing time.
     *
     * @return sizes of color sizes
     */
    private int[] getOptimalDivision() {
        int n = graph.getVertices();

        /*
         * Count how would n would be divided between machines
         * if sizes of color classes were represented as real numbers.
         */
        double[] nFloat = new double[3];
        for (int i = 0; i < speeds.length; i++) {
            nFloat[i] = n * speeds[i] / sumOfSpeeds;
        }

        /*
         * Calculate intermediate results to simplify
         * calculations.
         */
        double time2Floor = Math.floor(nFloat[1]) / speeds[MIDDLE];
        double time2Ceil = Math.ceil(nFloat[1]) / speeds[MIDDLE];
        double time3Floor = Math.floor(nFloat[2]) / speeds[FASTEST];
        double time3Ceil = Math.ceil(nFloat[2]) / speeds[FASTEST];
        double totalTime = n / sumOfSpeeds;

        /*
         * Calculate possible variants of total processing
         * time depending on how we round up the sizes
         * of perfect color classes which are real numbers.
         */
        double maxTime1 = Math.max(time3Floor, Math.max(time2Ceil, totalTime - time3Floor - time2Ceil));
        double maxTime2 = Math.max(time3Ceil, Math.max(time2Floor, totalTime - time3Ceil - time2Floor));
        double maxTime3 = Math.max(time3Ceil, Math.max(time2Ceil, totalTime - time3Ceil - time2Ceil));

        double minTime = Math.min(maxTime1, Math.min(maxTime2, maxTime3));

        int[] division = new int[3];

        if (minTime == maxTime1) {
            division[2] = (int) Math.floor(nFloat[2]);
            division[1] = (int) Math.ceil(nFloat[1]);
        } else if (minTime == maxTime2) {
            division[2] = (int) Math.ceil(nFloat[FASTEST]);
            division[1] = (int) Math.floor(nFloat[MIDDLE]);
        } else {
            division[2] = (int) Math.ceil(nFloat[FASTEST]);
            division[1] = (int) Math.ceil(nFloat[MIDDLE]);
        }

        division[SLOWEST] = n - division[MIDDLE] - division[FASTEST];

        return division;
    }

    /**
     * Split color class of size n / 2 between two
     * slower machines.
     *
     * @param sizeOfC of smallest color class
     */
    private void splitBetweenSlowerMachines(int sizeOfC) {
        int index = 0;

        while (sizeOfC-- != 0) {
            if (coloring.get(index) == B) {
                coloring.set(index, C);
            }
        }
    }
}
