package graph;

/**
 * Created by Paweł Kopeć on 17.03.17.
 *
 * Graph subclass that enables to take control over
 * whether graph is regular.
 */
public interface RegularGraph extends Graph {

    static RegularGraph getInstance(int verticesNumber, int degree) {
        System.out.println("O huj chodzi");
        return null;
    }

    int getDegree();

    boolean isRegular();
}
