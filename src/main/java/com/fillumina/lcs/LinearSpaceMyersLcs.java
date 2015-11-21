package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Myers Linear LCS Algorithm for collections and arrays.
 *
 * @see AbstractLinearSpaceMyersLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs extends AbstractLinearSpaceMyersLcs {
    private final Object[] a, b;

    public static <T> LinearSpaceMyersLcs lcs(
            final Collection<? extends T> a,
            final Collection<? extends T> b) {
        final int n = a.size();
        final int m = b.size();
        @SuppressWarnings("unchecked")
        final T[] oa = (T[]) a.toArray(new Object[n]);
        @SuppressWarnings("unchecked")
        final T[] ob = (T[]) b.toArray(new Object[m]);
        return new LinearSpaceMyersLcs(oa, ob);
    }

    public static <T> LinearSpaceMyersLcs lcs(final T[] oa, final T[] ob) {
        return new LinearSpaceMyersLcs(oa, ob);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getLcsList() {
        final List<T> lcs = new ArrayList<>(getLcsLength());
        for (int index : lcsIndexesOfTheFirstSequence()) {
            lcs.add((T) a[index]);
        }
        return lcs;
    }

    private LinearSpaceMyersLcs(Object[] a, Object[] b) {
        this.a = a;
        this.b = b;
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
