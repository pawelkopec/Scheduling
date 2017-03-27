package scheduling.three;

import graph.RegularGraph;
import graph.VertexColoring;

import static java.lang.Math.*;

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
        if (speeds[Const.FASTEST] < speeds[Const.SLOWEST] + speeds[Const.MIDDLE]) {
            //TODO check sheet

            findOptimalDivision();
            splitBetweenSlowerMachines(division[Const.SLOWEST]);

            ClwWithConstantB clw = new ClwWithConstantB(graph, coloring);

            int verticesToMove = division[Const.SLOWEST] - (graph.getVertices() / 2 - division[Const.MIDDLE]);
            clw.moveVertices(verticesToMove);
        }
        else {
            findOptimalDivision2();
            splitBetweenSlowerMachines(division[Const.SLOWEST]);
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
        double timeMiddleFloor = floor(nFloat[Const.MIDDLE]) / speeds[Const.MIDDLE];
        double timeMiddleCeil = ceil(nFloat[Const.MIDDLE]) / speeds[Const.MIDDLE];
        double timeFastestFloor = floor(nFloat[Const.FASTEST]) / speeds[Const.FASTEST];
        double timeFastestCeil = ceil(nFloat[Const.FASTEST]) / speeds[Const.FASTEST];
        double timeSlowest1 = ((double) n - floor(nFloat[Const.FASTEST]) - ceil(nFloat[Const.MIDDLE])) / speeds[Const.SLOWEST];
        double timeSlowest2 = ((double) n - ceil(nFloat[Const.FASTEST]) - floor(nFloat[Const.MIDDLE])) / speeds[Const.SLOWEST];
        double timeSlowest3 = ((double) n - ceil(nFloat[Const.FASTEST]) - ceil(nFloat[Const.MIDDLE])) / speeds[Const.SLOWEST];

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
            division[Const.FASTEST] = (int) floor(nFloat[Const.FASTEST]);
            division[Const.MIDDLE] = (int) ceil(nFloat[Const.MIDDLE]);
        } else if (minTime == maxTime2) {
            division[Const.FASTEST] = (int) ceil(nFloat[Const.FASTEST]);
            division[Const.MIDDLE] = (int) floor(nFloat[Const.MIDDLE]);
        } else {
            division[Const.FASTEST] = (int) ceil(nFloat[Const.FASTEST]);
            division[Const.MIDDLE] = (int) ceil(nFloat[Const.MIDDLE]);
        }

        division[Const.SLOWEST] = n - division[Const.MIDDLE] - division[Const.FASTEST];
    }

    /**
     * Determine how to split one color class of size n / 2
     * evenly between two slower machines.
     */
    private void findOptimalDivision2() {
        division = new int[3];
        division[Const.FASTEST] = graph.getVertices() / 2;
        division[Const.MIDDLE] = (int) ceil((double)(graph.getVertices() / 2) * speeds[Const.MIDDLE] / (speeds[Const.MIDDLE] + speeds[Const.SLOWEST]));
        division[Const.SLOWEST] = (graph.getVertices() / 2) - division[Const.MIDDLE];

        double maxTime1 = max((double) division[Const.MIDDLE] / speeds[Const.MIDDLE], (double) division[Const.SLOWEST] / speeds[Const.SLOWEST]);
        double maxTime2 = max(((double) division[Const.MIDDLE] - 1.) / speeds[Const.MIDDLE], ((double) division[Const.SLOWEST] + 1.) / speeds[Const.SLOWEST]);

        if (maxTime2 < maxTime1) {
            division[Const.SLOWEST]++;
            division[Const.MIDDLE]--;
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
