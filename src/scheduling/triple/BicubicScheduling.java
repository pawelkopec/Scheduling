package scheduling.triple;

import graph.RegularGraph;
import graph.VertexColoring;

import static java.lang.Math.*;
import static scheduling.triple.Const.*;

/**
 * Created by Paweł Kopeć on 02.03.17.
 *
 * Class for implementing scheduling of unit-length
 * jobs for 3 machines on 2-chromatic cubic graph.
 */
class BicubicScheduling extends CubicScheduling {

    public BicubicScheduling(RegularGraph graph, VertexColoring coloring, double[] speeds) {
        super(graph, coloring, speeds);
    }

    public BicubicScheduling(RegularGraph graph, double[] speeds) {
        super(graph, speeds);
    }

    public VertexColoring findColoring() {
        if (speeds[FASTEST] < speeds[SLOWEST] + speeds[MIDDLE]) {
            //TODO check sheet

            findOptimalDivision();
            splitBetweenSlowerMachines(division[SLOWEST]);

            ClwWithConstantB clw = new ClwWithConstantB(graph, coloring);

            int verticesToMove = division[SLOWEST] - (graph.getVertices() / 2 - division[MIDDLE]);
            clw.moveVertices(verticesToMove);
        }
        else {
            findOptimalDivision2();
            splitBetweenSlowerMachines(division[SLOWEST]);
        }

        return coloring;
    }

    /**
     * Check how to round up optimal sizes of color classes
     * to integers to obtain minimal processing time.
     */
    private void findOptimalDivision() {
        int n = graph.getVertices();

        /*
         * Count how would n would be divided between machines
         * if sizes of color classes were represented as real numbers.
         */
        double[] nFloat = new double[3];
        for (int i = 0; i < speeds.length; i++) {
            nFloat[i] = (double) n * speeds[i] / sumOfSpeeds;
        }

        /*
         * Calculate intermediate results to simplify
         * calculations.
         */
        double timeMiddleFloor = floor(nFloat[MIDDLE]) / speeds[MIDDLE];
        double timeMiddleCeil = ceil(nFloat[MIDDLE]) / speeds[MIDDLE];
        double timeFastestFloor = floor(nFloat[FASTEST]) / speeds[FASTEST];
        double timeFastestCeil = ceil(nFloat[FASTEST]) / speeds[FASTEST];
        double timeSlowest1 = ((double) n - floor(nFloat[FASTEST]) - ceil(nFloat[MIDDLE])) / speeds[SLOWEST];
        double timeSlowest2 = ((double) n - ceil(nFloat[FASTEST]) - floor(nFloat[MIDDLE])) / speeds[SLOWEST];
        double timeSlowest3 = ((double) n - ceil(nFloat[FASTEST]) - ceil(nFloat[MIDDLE])) / speeds[SLOWEST];

        /*
         * Calculate possible variants of total processing
         * time depending on how we round up the sizes
         * of perfect color classes which sizes are real numbers.
         */
        double maxTime1 = max(timeFastestFloor, max(timeMiddleCeil, timeSlowest1));
        double maxTime2 = max(timeFastestCeil, max(timeMiddleFloor, timeSlowest2));
        double maxTime3 = max(timeFastestCeil, max(timeMiddleCeil, timeSlowest3));

        double minTime = min(maxTime1, min(maxTime2, maxTime3));

        division = new int[3];

        if (minTime == maxTime1) {
            division[FASTEST] = (int) floor(nFloat[FASTEST]);
            division[MIDDLE] = (int) ceil(nFloat[MIDDLE]);
        } else if (minTime == maxTime2) {
            division[FASTEST] = (int) ceil(nFloat[FASTEST]);
            division[MIDDLE] = (int) floor(nFloat[MIDDLE]);
        } else {
            division[FASTEST] = (int) ceil(nFloat[FASTEST]);
            division[MIDDLE] = (int) ceil(nFloat[MIDDLE]);
        }

        division[SLOWEST] = n - division[MIDDLE] - division[FASTEST];
    }

    /**
     * Determine how to split one color class of size n / 2
     * evenly between two slower machines.
     */
    private void findOptimalDivision2() {
        division = new int[3];
        division[FASTEST] = graph.getVertices() / 2;
        division[MIDDLE] = (int) ceil((double)(graph.getVertices() / 2) * speeds[MIDDLE] / (speeds[MIDDLE] + speeds[SLOWEST]));
        division[SLOWEST] = (graph.getVertices() / 2) - division[MIDDLE];

        double maxTime1 = max((double) division[MIDDLE] / speeds[MIDDLE], (double) division[SLOWEST] / speeds[SLOWEST]);
        double maxTime2 = max(((double) division[MIDDLE] - 1.) / speeds[MIDDLE], ((double) division[SLOWEST] + 1.) / speeds[SLOWEST]);

        if (maxTime2 < maxTime1) {
            division[SLOWEST]++;
            division[MIDDLE]--;
        }
    }

    /**
     * Split color class of size n / 2 between two
     * slower machines.
     *
     * @param sizeOfC of smallest color class
     */
    private void splitBetweenSlowerMachines(int sizeOfC) {
        int index = 0;

        while (0 < sizeOfC) {
            if (coloring.get(index) == Const.B) {
                coloring.set(index, Const.C);
                sizeOfC--;
            }
            index++;
        }
    }

    @Override
    public int[] getDivision() {
        return division;
    }
}
