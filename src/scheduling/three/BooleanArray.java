package scheduling.three;

import java.util.BitSet;

/**
 * Created by vivace on 08.04.17.
 * <p>
 * Array of booleans represented as bits
 * with built in counting of all ones.
 */
public class BooleanArray extends BitSet {

    private int count = 0;

    BooleanArray(int size) {
        super(size);
    }

    @Override
    public void set(int bitIndex) {
        if (!get(bitIndex)) {
            count++;
        }

        super.set(bitIndex);
    }

    @Override
    public void set(int bitIndex, boolean value) {
        if (get(bitIndex) && !value) {
            count--;
        } else if (!get(bitIndex) && value) {
            count++;
        }

        super.set(bitIndex, value);
    }

    public int getCount() {
        return count;
    }
}
