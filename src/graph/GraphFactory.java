package graph;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Class for automation of Graph subclasses constructing
 */
public class GraphFactory {

    public static Graph getInstance(String className, int verticesNumber, int edgesNumber) throws ClassNotFoundException, IllegalArgumentException {
        Class graphClass = Class.forName(className);

        if (Graph.class.isAssignableFrom(graphClass)) {
            switch (graphClass.getName()) {
                case "graph.ListGraph":
                    return new ListGraph(verticesNumber);
                case "graph.MatrixGraph":
                    return new MatrixGraph(verticesNumber, edgesNumber);
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
}
