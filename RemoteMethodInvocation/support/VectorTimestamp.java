package support;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tiagoalexbastos on 20-05-2017.
 */
public class VectorTimestamp implements Cloneable, Serializable {

    private static final long serialVersionUID = 999821105988480755L;
    private int[] timestamps;
    private int index;

    public VectorTimestamp(int size, int index) {
        this.timestamps = new int[size];
        this.index = index;
    }

    public void increment() {
        timestamps[index]++;
    }

    public void specificIncrement(int idx){
        timestamps[idx]++;
    }

    public void update(VectorTimestamp vt) {
        for (int i = 0; i < timestamps.length; i++)
            timestamps[i] = Math.max(timestamps[i], vt.timestamps[i]);
    }

    public int[] toIntArray() {

        return timestamps;
    }

    @Override
    public VectorTimestamp clone() {
        VectorTimestamp copy = null;

        try {
            copy = (VectorTimestamp) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(VectorTimestamp.class.getName()).log(Level.SEVERE, null, ex);
        }

        copy.index = index;
        copy.timestamps = Arrays.copyOf(timestamps, timestamps.length);

        return copy;
    }

}
