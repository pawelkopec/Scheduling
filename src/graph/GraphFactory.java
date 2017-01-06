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

    public enum GRAPH_TYPES { LIST_GRAPH, MATRIX_GRAPH }

    public static Graph getInstance(GRAPH_TYPES type, int verticesNumber) {
        switch (type) {
            case LIST_GRAPH:
                return new ListGraph(verticesNumber);
            case MATRIX_GRAPH:
                return new MatrixGraph(verticesNumber);
            default:
                return null;
        }
    }

    public static Graph getInstance(GRAPH_TYPES type) {
        return GraphFactory.getInstance(type, 0);
    }

    public static Graph getInstanceFromString(GRAPH_TYPES type, String s) {
        switch (type) {
            case LIST_GRAPH:
                return new ListGraph(s);
            case MATRIX_GRAPH:
                return new MatrixGraph(s);
            default:
                return null;
        }
    }

    public static Graph getInstanceFromStream(GRAPH_TYPES type, InputStream in) {
        switch (type) {
            case LIST_GRAPH:
                return new ListGraph(in);
            case MATRIX_GRAPH:
                return new MatrixGraph(in);
            default:
                return null;
        }
    }
}
