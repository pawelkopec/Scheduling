package graph.util;

import graph.RegularGraph;

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
    private int d2; //Number of vertices from first partition which must have 2 edges with second partition
    private int d3; //Number of vertices from first partition which must have 2 edges with third partition
    private int currd2; //Number of vertices from first partition which actually have 2 edges with second partition
    private int currd3; //Number of vertices from first partition which actually have 2 edges with third partition

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
    public <G extends RegularGraph> G getRandomGraph(Class<G> clazz, int verticesNumber, int degree, ALGORITHMS algorithm) throws IllegalArgumentException {
        d2 = secondPartitionSize*3-verticesNumber/2;
        d3 = thirdPartitionSize*3-verticesNumber/2;

        return super.getRandomGraph(clazz, verticesNumber, degree, algorithm);
    }

    @Override
    protected void startLoop() {
        currd2 = 0;
        currd3 = 0;
    }

    @Override
    protected boolean areSuitable(int a, int b, List<Integer> pairs, int verticesNumber, int degree) {
        int aGroup = getPointGroup(a, verticesNumber);
        int bGroup = getPointGroup(b, verticesNumber);
        int aGroupPartition = getGroupPartition(aGroup);
        int bGroupPartition = getGroupPartition(bGroup);
        List<Integer> aGroupConnections, bGroupConnections;
        aGroupConnections = getGroupConnections(pairs, aGroup, verticesNumber, degree);
        bGroupConnections = getGroupConnections(pairs, bGroup, verticesNumber, degree);

        if (aGroupPartition == bGroupPartition) {
            return false;
        }


        if (((aGroupPartition == 2 && bGroupPartition == 3) || (aGroupPartition == 3 && bGroupPartition == 2))) {
            return false;
        }

        if (aGroupPartition == 1) {
            if (aGroupConnections.stream().filter(group -> getGroupPartition(group) == bGroupPartition).count() == 2) {
                return false;
            }

            if (bGroupPartition == 3 && aGroupConnections.stream().filter(group -> getGroupPartition(group) == 3).count() == 1 && currd3 == d3) {
                return false;
            }

            if (bGroupPartition == 2 && aGroupConnections.stream().filter(group -> getGroupPartition(group) == 2).count() == 1 && currd2 == d2) {
                return false;
            }
        }
        else {
            if (bGroupConnections.stream().filter(group -> getGroupPartition(group) == aGroupPartition).count() == 2) {
                return false;
            }

            if (aGroupPartition == 3 && currd3 == d3) {
                return false;
            }

            if (aGroupPartition == 2 && currd2 == d2) {
                return false;
            }
        }

        return super.areSuitable(a, b, pairs, verticesNumber, degree);
    }

    @Override
    protected void setSuitable(Integer i, Integer j, List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        int aGroup = getPointGroup(i, verticesNumber);
        int bGroup = getPointGroup(j, verticesNumber);
        int aGroupPartition = getGroupPartition(aGroup);
        int bGroupPartition = getGroupPartition(bGroup);
        List<Integer> aGroupConnections, bGroupConnections;
        aGroupConnections = getGroupConnections(pairs, aGroup, verticesNumber, degree);
        bGroupConnections = getGroupConnections(pairs, bGroup, verticesNumber, degree);

        if (bGroupPartition == 2 && aGroupConnections.stream().filter(group -> getGroupPartition(group) == 2).count() == 1) {
            currd2++;
        }

        if (bGroupPartition == 3 && aGroupConnections.stream().filter(group -> getGroupPartition(group) == 3).count() == 1) {
            currd3++;
        }
    }

    @Override
    protected Integer getFirstPoint(Random rand, List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        List<Integer> firstPartitionPoints;
        firstPartitionPoints = points.stream().
                filter(point -> getGroupPartition(getPointGroup(point, verticesNumber)) == 1).
                collect(Collectors.toList());
        firstPoint = firstPartitionPoints.get(rand.nextInt(firstPartitionPoints.size()));
        return firstPoint;
    }

    @Override
    protected Integer getSecondPoint(Random rand, List<Integer> points, List<Integer> pairs, int verticesNumber, int degree) {
        int firstPointGroup = getPointGroup(firstPoint, verticesNumber);
        List<Integer> firstPointGroupConnections = getGroupConnections(pairs, firstPointGroup, verticesNumber, degree);
        int firstPointGroupPartition = getGroupPartition(firstPointGroup);

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
