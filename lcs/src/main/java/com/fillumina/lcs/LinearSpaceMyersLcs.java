package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Myers Linear LCS Algorithm for collections and arrays. This implementation
 * provides the LCS for collections and arrays.
 *
 * @see AbstractLinearSpaceMyersLcSequence
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs extends AbstractLinearSpaceMyersLcSequence {
    private final Object[] a, b;

    /**
     * Performs the LCS calculation and return an object that can be
     * queried for the results.
     * @param <T> type of the lists
     * @param a   first sequence
     * @param b   second sequence
     * @return    result object that can be queried for various data
     */
    public static <T> LinearSpaceMyersLcs lcs(
            final Collection<? extends T> a,
            final Collection<? extends T> b) {
        final int n = a.size();
        final int m = b.size();
        @SuppressWarnings("unchecked")
        final T[] oa = (T[]) a.toArray(new Object[n]);
        @SuppressWarnings("unchecked")
        final T[] ob = (T[]) b.toArray(new Object[m]);
        final LinearSpaceMyersLcs linearSpaceMyersLcs =
                new LinearSpaceMyersLcs(oa, ob);
        linearSpaceMyersLcs.calculateLcs();
        return linearSpaceMyersLcs;
    }

    /**
     * Performs the LCS calculation and return an object that can be
     * queried for the results.
     * @param <T> type of the lists
     * @param oa  first sequence
     * @param ob  second sequence
     * @return    result object that can be queried for various data
     */
    public static <T> LinearSpaceMyersLcs lcs(final T[] oa, final T[] ob) {
        final LinearSpaceMyersLcs linearSpaceMyersLcs =
                new LinearSpaceMyersLcs(oa, ob);
        linearSpaceMyersLcs.calculateLcs();
        return linearSpaceMyersLcs;
    }

    private LinearSpaceMyersLcs(Object[] a, Object[] b) {
        this.a = a;
        this.b = b;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> calcaulateLcsList() {
        final List<T> lcs = new ArrayList<>(getLcsLength());
        for (int index : lcsIndexesOfTheFirstSequence()) {
            lcs.add((T) a[index]);
        }
        return lcs;
    }

    @Override
    public boolean equals(int x, int y) {
        return Objects.equals(a[x], b[y]);
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
