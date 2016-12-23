package graph;

import java.util.NoSuchElementException;

/**
 * Created by vivace on 23.12.16.
 */
public interface Colorable {

    void addColor(int colorIndex) throws NoSuchElementException;

    void removeColor(int colorIndex) throws  NoSuchElementException;

    boolean hasColor(int colorIndex) throws  NoSuchElementException;
}
