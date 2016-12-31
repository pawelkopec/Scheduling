package graph;

import java.io.InputStream;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Class for automation of Graph subclasses constructing
 *
 *TODO
 * Think of some meta-programming to get rid of ugly repeating
 * of if-else and switch statements in different getInstance(...).
 */
public class GraphFactory {

    public static Graph getInstance(String className, int verticesNumber, int edgesNumber) {
        Class graphClass = validateGraphClassName(className);

        switch (graphClass.getName()) {
            case "graph.ListGraph":
                return new ListGraph(verticesNumber);
            case "graph.MatrixGraph":
                return new MatrixGraph(verticesNumber);
            default:
                throw new IllegalArgumentException(className + " cannot be initialized as Graph.");

        }
    }

    public static Graph getInstance(String className, int verticesNumber) throws ClassNotFoundException, IllegalArgumentException {
        return GraphFactory.getInstance(className, verticesNumber, 0);
    }

    public static Graph getInstance(String className) throws ClassNotFoundException, IllegalArgumentException {
        return GraphFactory.getInstance(className, 0);
    }

    public static Graph getInstanceFromString(String className, String s) throws ClassNotFoundException {
        Class graphClass = validateGraphClassName(className);

        switch (graphClass.getName()) {
            case "graph.ListGraph":
                return new ListGraph(s);
            case "graph.MatrixGraph":
                return new MatrixGraph(s);
            default:
                throw new IllegalArgumentException(className + " cannot be initialized as Graph.");
        }
    }

    public static Graph getInstanceFromStream(String className, InputStream in) throws ClassNotFoundException {
        Class graphClass = validateGraphClassName(className);

        switch (graphClass.getName()) {
            case "graph.ListGraph":
                return new ListGraph(in);
            case "graph.MatrixGraph":
                return new MatrixGraph(in);
            default:
                throw new IllegalArgumentException(className + " cannot be initialized as Graph.");
        }
    }

    private static Class validateGraphClassName(String className) throws IllegalArgumentException {
        Class graphClass = null;

        try {
            graphClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(className + " is not a valid class name.");
        }

        if(!Graph.class.isAssignableFrom(graphClass)) {
            throw new IllegalArgumentException(className + " is not a valid Graph subclass name.");
        }

        return graphClass;
    }
}
