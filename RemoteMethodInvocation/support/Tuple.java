package support;

import java.io.Serializable;

/**
 * Created by tiagoalexbastos on 20-05-2017.
 */
public class Tuple<X, Y> implements Serializable {


    private static final long serialVersionUID = -7002930729616686632L;
    private final X clock;
    private final Y second;

    public Tuple(X clock, Y second) {
        this.clock = clock;
        this.second = second;
    }

    public X getClock() {
        return clock;
    }

    public Y getSecond() {
        return second;
    }
}
