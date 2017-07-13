package support;

/**
 * Created by tiagoalexbastos on 01-06-2017.
 */
public class LineUpdate implements Comparable<LineUpdate>{
    private String line;
    private VectorTimestamp vt;

    public LineUpdate(String line, VectorTimestamp vt) {
        this.line = line;
        this.vt = vt;
    }

    public String getLine() {
        return line;
    }

    public VectorTimestamp getVt() {
        return vt;
    }

    public int compareTo(LineUpdate update) {
        int elem1[] = this.vt.toIntArray();
        int elem2[] = update.vt.toIntArray();

        // Check if my timestamp comes first

        boolean all_le = true;

        for(int i = 0; i < elem1.length; i++) {
            if (elem2[i] > elem1[i]) {
                all_le = false;
                break;
            }
        }

        if (all_le) {
            for(int i = 0; i < elem1.length; i++) {
                if (elem1[i] < elem2[i]) return -1;
            }
        }

        // Check if the other timestamp comes first

        all_le = true;

        for(int i = 0; i < elem1.length; i++) {
            if (elem1[i] > elem2[i]) {
                all_le = false;
                break;
            }
        }

        if (all_le) {
            for(int i = 0; i < elem1.length; i++) {
                if (elem2[i] < elem1[i]) return 1;
            }
        }

        return 0;

    }


    @Override
    public String toString() {
        return line;
    }

}
