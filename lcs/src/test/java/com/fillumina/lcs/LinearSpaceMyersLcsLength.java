package com.fillumina.lcs;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsLength
        extends AbstractLinearSpaceMyersLcIndex {

    private final Object[] a;
    private final Object[] b;
    private int lcs;

    public LinearSpaceMyersLcsLength(
            List<?> a,
            List<?> b) {
        this.a = a.toArray(new Object[a.size()]);
        this.b = b.toArray(new Object[b.size()]);
        calculateLcs();
    }

    public int getLcs() {
        return lcs;
    }

    @Override
    protected int getFirstSequenceLength() {
        return a.length;
    }

    @Override
    protected int getSecondSequenceLength() {
        return b.length;
    }

    @Override
    protected boolean equals(int x, int y) {
        return Objects.equals(a[x], b[y]);
    }

    @Override
    protected LcsItem match(int x, int y, int steps) {
        lcs += steps;
        return null;
    }
}
