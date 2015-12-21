package com.fillumina.lcs;

import java.util.List;

/**
 * Abstract template that implements the head-tail equal elements optimization.
 *
 * @author Francesco Illuminati 
 */
abstract class AbstractLcsHeadTailReducer {
    private int counter = -1;

    public AbstractLcsHeadTailReducer() {
    }

    /** Use this constructor if you want to only calculate the size of the LCS. */
    public AbstractLcsHeadTailReducer(final boolean sizeOnly) {
        if (sizeOnly) {
            counter = 0;
        }
    }

    /** @return the length of the first sequence. */
    protected abstract int getFirstSequenceLength();

    /** @return the length of the second sequence. */
    protected abstract int getSecondSequenceLength();

    /** @return {@code true} when the elements at the specified indexes
     *          matches.
     */
    protected abstract boolean sameAtIndex(int x, int y);

    /** Template method called by {@link #lcsHeadTail(int, int, int, int)}. */
    abstract LcsItemImpl lcs(int a0, int n, int b0, int m);

    public List<LcsItem> calculateLcs() {
        return lcsHeadTail(0, getFirstSequenceLength(),
                0, getSecondSequenceLength());
    }

    public int calculateLcsLength() {
        final List<LcsItem> list = calculateLcs();
        if (counter == -1) {
            return list.size();
        }
        return counter;
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
    LcsItemImpl match(int x, int y, int steps) {
        if (counter == -1) {
            return new LcsItemImpl(x, y, steps);
        } else {
            counter += steps;
            return null;
        }
    }

    /**
     * Optimizes the calculation by filtering out equal elements on the head
     * and tail of the sequences.
     */
    final LcsItemImpl lcsHeadTail(
            final int a0, final int n,
            final int b0, final int m) {
        final int min = n < m ? n : m;
        LcsItemImpl matchDown = null;
        int d;
        for (d = 0; d < min && sameAtIndex(a0 + d, b0 + d); d++) {}
        if (d > 0) {
            matchDown = match(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        LcsItemImpl matchUp = null;
        final int x0 = a0 + n - 1;
        final int y0 = b0 + m - 1;
        final int maxu = min - d;
        int u;
        for (u = 0; u < maxu && sameAtIndex(x0 - u, y0 - u); u++) {}
        if (u > 0) {
            matchUp = match(a0 + n - u, b0 + m - u, u);
        }
        LcsItemImpl lcsMatch = null;
        if (u + d < min) {
            lcsMatch = lcs(a0+d, n-d-u, b0+d, m-d-u);
        }
        return LcsItemImpl.chain(matchDown, lcsMatch, matchUp);
    }

}
