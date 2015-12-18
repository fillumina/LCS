package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class DefaultLcsInput<T> implements LcsInput {
    private final T[] a, b;

    /**
     * Performs the LCS calculation and return an object that can be
     * queried for the results.
     * @param <T> type of the lists
     * @param a   first sequence
     * @param b   second sequence
     * @return    result object that can be queried for various data
     */
    @SuppressWarnings("unchecked")
    public DefaultLcsInput(
            final Collection<? extends T> a,
            final Collection<? extends T> b) {
        this((T[]) a.toArray(), (T[]) b.toArray());
    }

    public DefaultLcsInput(T[] a, T[] b) {
        this.a = a;
        this.b = b;
    }

    @SuppressWarnings("unchecked")
    public List<T> extractLcsList(List<LcsItem> lcsItem) {
        if (lcsItem == null) {
            return Collections.<T>emptyList();
        }
        final List<T> lcs = new ArrayList<>(lcsItem.size());
        for (int index : lcsItem.get(0).lcsIndexesOfTheFirstSequence()) {
            lcs.add(a[index]);
        }
        return lcs;
    }

    @Override
    public final boolean equals(final int i, final int j) {
        final T x = a[i];
        final T y = b[j];
        return (x == y) || (x != null && x.equals(y));
    }

    @Override
    public int getFirstSequenceLength() {
        return a == null ? 0 : a.length;
    }

    @Override
    public int getSecondSequenceLength() {
        return b == null ? 0 : b.length;
    }
}
