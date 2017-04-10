package graph.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by robert on 09.04.17.
 */
public class CreepyBipartiteRegularGraphGenerator extends RegularGraphGenerator {
    private int firstPartitionSize = 0;
    private int secondPartitionSize = 0;
    private int thirdPartitionSize = 0;
    private int firstPoint;
    private int secondPoint;

    protected int getGroupPartition(int group) {
        if (group < firstPartitionSize) {
            return 1;
        }
        else if (group < firstPartitionSize + secondPartitionSize) {
            return 2;
        }
        else {
            return 3;
        }
    }

    protected List<Integer> getGroupConnections(List<Integer> pairs, int group, int verticesNumber, int degree) {
        List<Integer> connections = new ArrayList<>(degree);
        for (int i = 0; i < degree; i++) {
            connections.add(getPointGroup(pairs.get((group % verticesNumber == 0 ? verticesNumber : group) + verticesNumber*i), verticesNumber));
        }
        return connections;
    }

    @Override
    protected boolean areSuitable(int a, int b, List<Integer> pairs, int verticesNumber, int degree) {
        int aGroup = getPointGroup(a, verticesNumber);
        int bGroup = getPointGroup(b, verticesNumber);
        int aGroupPartition = getGroupPartition(aGroup);
        int bGroupPartition = getGroupPartition(bGroup);
        List<Integer> aGroupConnections, bGroupConnections;

        if (aGroupPartition == bGroupPartition) {
            return false;
        }


        if (((aGroupPartition == 2 && bGroupPartition == 3) || (aGroupPartition == 3 && bGroupPartition == 2))) {
            return false;
        }

        if (aGroupPartition == 1) {
            aGroupConnections = getGroupConnections(pairs, aGroup, verticesNumber, degree);
            if (aGroupConnections.stream().filter(group -> getGroupPartition(group) == bGroupPartition).count() == 2) {
                return false;
            }
        }
        else {
            bGroupConnections = getGroupConnections(pairs, bGroup, verticesNumber, degree);
            if (bGroupConnections.stream().filter(group -> getGroupPartition(group) == aGroupPartition).count() == 2) {
                return false;
            }
        }

        return super.areSuitable(a, b, pairs, verticesNumber, degree);
    }

    @Override
    protected Integer getFirstPoint(Random rand, List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        List<Integer> firstPartitionPoints;
        firstPartitionPoints = points.stream().
                filter(point -> getGroupPartition(getPointGroup(point, verticesNumber)) == 1).
                collect(Collectors.toList());
        firstPoint = firstPartitionPoints.get(rand.nextInt(firstPartitionPoints.size()));
        return firstPoint = points.get(rand.nextInt(points.size()));
    }

    @Override
    protected Integer getSecondPoint(Random rand, List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        int firstPointGroupPartition = getGroupPartition(getPointGroup(firstPoint, verticesNumber));
        List<Integer> otherPoints = points.stream().
                filter(point -> getGroupPartition(getPointGroup(point, verticesNumber)) != firstPointGroupPartition).
                collect(Collectors.toList());

        return secondPoint = otherPoints.get(rand.nextInt(otherPoints.size()));
    }

    public void setFirstPartitionSize(int firstPartitionSize) {
        this.firstPartitionSize = firstPartitionSize;
    }

    public void setSecondPartitionSize(int secondPartitionSize) {
        this.secondPartitionSize = secondPartitionSize;
    }

    public void setThirdPartitionSize(int thirdPartitionSize) {
        this.thirdPartitionSize = thirdPartitionSize;
    }

    public void setPartitionsSize(int firstPartitionSize, int secondPartitionSize, int thirdPartitionSize) {
        setFirstPartitionSize(firstPartitionSize);
        setSecondPartitionSize(secondPartitionSize);
        setThirdPartitionSize(thirdPartitionSize);
    }

}
