package graph;

/**
 * Created by Paweł Kopeć on 09.02.17.
 *
 * Base class for assignment int values to vertices.
 */
public abstract class VertexAssignment {

    protected Graph graph;

    protected VertexAssignment(Graph graph) {
        if(graph == null) {
            throw new NullPointerException("Graph cannot be null.");
        }

        this.graph = graph;
    }

    public abstract int get(int index);

    public abstract void set(int index, int value);
}
