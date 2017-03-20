package graph;

/**
 * Created by Paweł Kopeć on 20.03.17.
 *
 * Extends GraphFactory to enable passing
 * a degree.
 */
public class RegularGraphFactory extends GraphFactory {
    public static <G extends RegularGraph> G getInstance(
            Class<G> graphSubclass, int verticesNumber, int degree) throws IllegalArgumentException {
        try {
            return graphSubclass.getDeclaredConstructor(Integer.TYPE, Integer.TYPE).newInstance(verticesNumber, degree);
        } catch (Exception e) {
            throw getTranslatedConstructorException(e);
        }
    }
}
