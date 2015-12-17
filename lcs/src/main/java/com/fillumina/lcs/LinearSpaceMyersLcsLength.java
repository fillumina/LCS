package com.fillumina.lcs;

import java.util.Collection;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsLength<T> extends LinearSpaceMyersLcs<T> {
    private int counter;

    @Override
    protected LcsItem match(final int x, final int y, final int steps) {
        counter += steps;
        return null;
    }

    @Override
    public int lcsLength(final Collection<? extends T> a,
            final Collection<? extends T> b) {
        super.lcsLength(a, b);
        return counter;
    }

    @Override
    public int lcsLength(final T[] a, final T[] b) {
        super.lcsLength(a, b);
        return counter;
    }
}
