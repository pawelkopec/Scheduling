package graph.test;

import graph.Graph;

/**
 * Created by Paweł Kopeć on 09.02.17.
 *
 * Base class for assignment int values to vertices.
 */
public abstract class VertexAssignment {

    protected Graph graph;

    protected VertexAssignment(Graph graph) {
        this.graph = graph;
    }

    public abstract int get(int index);

    public abstract void set(int index, int value);
}
