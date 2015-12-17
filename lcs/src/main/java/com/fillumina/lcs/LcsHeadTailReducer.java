package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class LcsHeadTailReducer<T> implements Lcs {

    protected abstract LcsItem lcs(final T status,
            final LcsInput lcsInput,
            final LcsSequencer seqGen,
            int a0, int n, int b0, int m);

    @Override
    @SuppressWarnings("unchecked")
    public final List<LcsItem> calculateLcs(final LcsInput lcsInput,
            final LcsSequencer seqGen) {
        final int n = lcsInput.getFirstSequenceLength();
        final int m = lcsInput.getSecondSequenceLength();
        if (n == 0 || m == 0) {
            return LcsItemImpl.NULL;
        }
        LcsItem result = lcsHeadTail(null, lcsInput, seqGen, 0, n, 0, m);
        if (result == null) {
            return LcsItemImpl.NULL;
        }
        return (List<LcsItem>) result;
    }

    /**
     * Optimizes the calculation by filtering out equal elements on the head
     * and tail of the sequences.
     */
    protected final LcsItem lcsHeadTail(final T status,
            final LcsInput lcsInput,
            final LcsSequencer seqGen,
            final int a0, final int n,
            final int b0, final int m) {
        final int min = n < m ? n : m;
        LcsItem matchDown = null;
        int d;
        for (d = 0; d < min && lcsInput.equals(a0 + d, b0 + d); d++) {}
        if (d > 0) {
            matchDown = seqGen.match(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        LcsItem matchUp = null;
        final int x0 = a0 + n - 1;
        final int y0 = b0 + m - 1;
        final int maxu = min - d;
        int u;
        for (u = 0; u < maxu && lcsInput.equals(x0 - u, y0 - u); u++) {}
        if (u > 0) {
            matchUp = seqGen.match(a0 + n - u, b0 + m - u, u);
        }
        LcsItem lcsMatch = null;
        if (u + d < min) {
            lcsMatch = lcs(status, lcsInput, seqGen, a0+d, n-d-u, b0+d, m-d-u);
        }
        return seqGen.chain(matchDown, lcsMatch, matchUp);
    }

}