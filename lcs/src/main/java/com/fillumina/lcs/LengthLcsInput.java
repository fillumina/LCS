package com.fillumina.lcs;

import java.util.Collection;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LengthLcsInput<T> extends DefaultLcsInput<T>
        implements LcsSequencer {
    private int counter;

    public LengthLcsInput(
            Collection<? extends T> a,
            Collection<? extends T> b) {
        super(a, b);
    }

    public LengthLcsInput(T[] a, T[] b) {
        super(a, b);
    }

    public int getLcsLength() {
        return counter;
    }

    @Override
    public LcsItem match(int x, int y, int steps) {
        counter += steps;
        return null;
    }

    @Override
    public LcsItem chain(LcsItem before, LcsItem middle, LcsItem after) {
        return null;
    }
}
