package graph;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Class providing universal interface
 * for constructing instances of Graph subclasses.
 */
public class GraphFactory {

    private static final String NO_SUCH_CONSTRUCTOR = "This subclass does not provide requested constructor.";
    private static final String NON_CALLABLE_CONSTRUCTOR = "This subclass is abstract and cannot be initialized.";
    private static final String NO_ACCESS_TO_CONSTRUCTOR = "This subclass does not give access to requested constructor.";
    private static final String CONSTRUCTOR_EXCPECTION = "This constructor threw its underlying exception.";

    /**
     * Unum for all Graph subclasses that provide a
     * constructor for given parameters of getInstance(...)
     * methods.
     *
     * For universal constructor interface
     * @see #getInstance(Class, int)
     * @see #getInstanceFromString(Class, String)
     * @see #getInstanceFromStream(Class, InputStream)
     */
    public enum GRAPH_TYPES { LIST_GRAPH, MATRIX_GRAPH }

    public static <G extends Graph> G getInstance(Class<G> graphSubclass, int verticesNumber) throws IllegalArgumentException {
        try {
            return graphSubclass.getDeclaredConstructor(Integer.TYPE).newInstance(verticesNumber);
        } catch (Exception e) {
            throw getTranslatedConstructorException(e);
        }
    }

    public static <G extends Graph> G getInstance(Class<G> graphSubclass) {
        return getInstance(graphSubclass, 0);
    }

    public static <G extends Graph> G getInstanceFromString(Class<G> graphSubclass, String s) throws IllegalArgumentException {
        try {
            return graphSubclass.getDeclaredConstructor(String.class).newInstance(s);
        } catch (Exception e) {
            throw getTranslatedConstructorException(e);
        }
    }

    public static <G extends Graph> G getInstanceFromStream(Class<G> graphSubclass, InputStream in) throws IllegalArgumentException {
        try {
            return graphSubclass.getDeclaredConstructor(InputStream.class).newInstance(in);
        } catch (Exception e) {
            throw getTranslatedConstructorException(e);
        }
    }

    public static BaseGraph getInstance(GRAPH_TYPES type, int verticesNumber) {
        switch (type) {
            case LIST_GRAPH:
                return new ListGraph(verticesNumber);
            case MATRIX_GRAPH:
                return new MatrixGraph(verticesNumber);
            default:
                return null;
        }
    }

    public static BaseGraph getInstance(GRAPH_TYPES type) {
        return GraphFactory.getInstance(type, 0);
    }

    public static BaseGraph getInstanceFromString(GRAPH_TYPES type, String s) {
        switch (type) {
            case LIST_GRAPH:
                return new ListGraph(s);
            case MATRIX_GRAPH:
                return new MatrixGraph(s);
            default:
                return null;
        }
    }

    public static BaseGraph getInstanceFromStream(GRAPH_TYPES type, InputStream in) {
        switch (type) {
            case LIST_GRAPH:
                return new ListGraph(in);
            case MATRIX_GRAPH:
                return new MatrixGraph(in);
            default:
                return null;
        }
    }

    protected static IllegalArgumentException getTranslatedConstructorException(Exception e) {
        if(e instanceof InstantiationException) {
            return new IllegalArgumentException(NON_CALLABLE_CONSTRUCTOR);
        }
        else if(e instanceof  IllegalAccessException) {
            return new IllegalArgumentException(NO_ACCESS_TO_CONSTRUCTOR);
        }
        else if(e instanceof InvocationTargetException) {
            return new IllegalArgumentException(CONSTRUCTOR_EXCPECTION + " " + e.getMessage());
        }
        else if(e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(NO_SUCH_CONSTRUCTOR);
        }
        else {
            return new IllegalArgumentException(e);
        }
    }
}
