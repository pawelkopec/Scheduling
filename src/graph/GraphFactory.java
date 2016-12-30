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

    public static Graph getInstance(String className, int verticesNumber, int edgesNumber) throws IllegalArgumentException, ClassNotFoundException {
        Class graphClass = null;
        try {
            graphClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(className + " is not a valid name for a class maintained by Graph Factory.");
        }

        if (Graph.class.isAssignableFrom(graphClass)) {
            switch (graphClass.getName()) {
                case "graph.ListGraph":
                    return new ListGraph(verticesNumber);
                case "graph.MatrixGraph":
                    return new MatrixGraph(verticesNumber);
                default:
                    throw new IllegalArgumentException(className + " cannot be initialized as Graph.");

            }
        } else {
            throw new IllegalArgumentException(className + " cannot be initialized as Graph.");
        }
    }

    public static Graph getInstance(String className, int verticesNumber) throws ClassNotFoundException, IllegalArgumentException {
        return GraphFactory.getInstance(className, verticesNumber, 0);
    }

    public static Graph getInstance(String className) throws ClassNotFoundException, IllegalArgumentException {
        return GraphFactory.getInstance(className, 0);
    }

    public static Graph getInstanceFromString(String className, String s) throws ClassNotFoundException, IllegalArgumentException {
        Class graphClass = null;
        try {
            graphClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(className + " is not a valid name for a class maintained by Graph Factory.");
        }

        if (Graph.class.isAssignableFrom(graphClass)) {
            switch (graphClass.getName()) {
                case "graph.ListGraph":
                    return new ListGraph(s);
                case "graph.MatrixGraph":
                    return new MatrixGraph(s);
                default:
                    throw new IllegalArgumentException(className + " cannot be initialized as Graph.");

            }
        } else {
            throw new IllegalArgumentException(className + " cannot be initialized as Graph.");
        }
    }

    public static Graph getInstanceFromStream(String className, InputStream in) throws ClassNotFoundException, IllegalArgumentException {
        Class graphClass = null;
        try {
            graphClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(className + " is not a valid name for a class maintained by Graph Factory.");
        }

        if (Graph.class.isAssignableFrom(graphClass)) {
            switch (graphClass.getName()) {
                case "graph.ListGraph":
                    return new ListGraph(in);
                case "graph.MatrixGraph":
                    return new MatrixGraph(in);
                default:
                    throw new IllegalArgumentException(className + " cannot be initialized as Graph.");

            }
        } else {
            throw new IllegalArgumentException(className + " cannot be initialized as Graph.");
        }
    }
}
