package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class LcsHeadTailReducer<T, S> implements Lcs<T> {

    abstract LcsItemImpl lcs(final S status,
            T[] a, int a0, int n, T[] b, int b0, int m);

    /** Override if you need a different equality. */
    protected boolean same(final Object a, final Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Override if you simply need to capture the LCS length
     * (in that case you can simply return {@code null} avoiding the
     * creation of unused objects):
     * <code><pre>
     * protected LcsItem match(int x, int y, int steps) {
     *   this.counter += steps;
     *   return null;
     * }
     * </pre></code>
     */
    protected LcsItem match(int x, int y, int steps) {
        return new LcsItemImpl(x, y, steps);
    }

    /** This is not optimized because it still creates the list. */
    @Override
    public int lcsLength(T[] a, T[] b) {
        return lcIndexes(a, b).size();
    }

    /** This is not optimized because it still creates the list. */
    @Override
    public int lcsLength(
            Collection<? extends T> a,
            Collection<? extends T> b) {
        return lcIndexes(a, b).size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final List<? extends T> lcSequence(final Collection<? extends T> a,
            final Collection<? extends T> b) {
        return lcSequence((T[]) a.toArray(new Object[a.size()]),
                (T[]) b.toArray(new Object[b.size()]));
    }

    @Override
    public final List<T> lcSequence(final T[] a, final T[] b) {
        final List<LcsItem> lcs = lcIndexes(a, b);
        final int size = lcs.size();
        if (size == 0) {
            return Collections.<T>emptyList();
        }
        final List<T> list = new ArrayList<>(size);
        for (int index : lcs.get(0).lcsIndexesOfTheFirstSequence()) {
            list.add(a[index]);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final List<LcsItem> lcIndexes(final Collection<? extends T> a,
            final Collection<? extends T> b) {
        return lcIndexes((T[]) a.toArray(new Object[a.size()]),
                (T[]) b.toArray(new Object[b.size()]));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<LcsItem> lcIndexes(final T[] a, final T[] b) {
        final int n = a.length;
        final int m = b.length;
        if (n == 0 || m == 0) {
            return LcsItemImpl.NULL;
        }
        LcsItem result = lcsHeadTail(null, a, 0, n, b, 0, m);
        if (result == null) {
            return LcsItemImpl.NULL;
        }
        return (List<LcsItem>) result;
    }

    /**
     * Optimizes the calculation by filtering out equal elements on the head
     * and tail of the sequences.
     */
    protected final LcsItem lcsHeadTail(final S status,
            final T[] a, final int a0, final int n,
            final T[] b, final int b0, final int m) {
        final int min = n < m ? n : m;
        LcsItemImpl matchDown = null;
        int d;
        for (d = 0; d < min && same(a[a0 + d], b[b0 + d]); d++) {}
        if (d > 0) {
            matchDown = (LcsItemImpl) match(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        LcsItemImpl matchUp = null;
        final int x0 = a0 + n - 1;
        final int y0 = b0 + m - 1;
        final int maxu = min - d;
        int u;
        for (u = 0; u < maxu && same(a[x0 - u], b[y0 - u]); u++) {}
        if (u > 0) {
            matchUp = (LcsItemImpl) match(a0 + n - u, b0 + m - u, u);
        }
        LcsItemImpl lcsMatch = null;
        if (u + d < min) {
            lcsMatch = lcs(status, a, a0+d, n-d-u, b, b0+d, m-d-u);
        }
        return LcsItemImpl.chain(matchDown, lcsMatch, matchUp);
    }

}
