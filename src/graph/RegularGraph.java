package graph;

/**
 * Created by Paweł Kopeć on 17.03.17.
 *
 * Graph subclass that enables to take control over
 * whether graph is regular.
 */
public interface RegularGraph extends Graph {

    int getDegree();

    boolean isRegular();
}
