package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
// TODO could it become a wrapper?
public abstract class LcsHeadTailReducer implements Lcs {

    protected abstract LcsItem lcs(LcsInput lcsInput,
            final LcsSequencer seqGen,
            int a0, int n, int b0, int m);

    @Override
    @SuppressWarnings("unchecked")
    public List<LcsItem> calculateLcs(final LcsInput lcsInput,
            final LcsSequencer seqGen) {
        final int n = lcsInput.getFirstSequenceLength();
        final int m = lcsInput.getSecondSequenceLength();
        if (n == 0 || m == 0) {
            return LcsItemImpl.NULL;
        }
        LcsItem result = lcsHeadTail(lcsInput, seqGen, 0, n, 0, m);
        if (result == null) {
            return LcsItemImpl.NULL;
        }
        return (List<LcsItem>) result;
    }

    /**
     * Optimizes the calculation by filtering out equal elements on the head
     * and tail of the sequences.
     */
    protected final LcsItem lcsHeadTail(final LcsInput lcsInput,
            final LcsSequencer seqGen,
            final int a0, final int n,
            final int b0, final int m) {
        final int min = n < m ? n : m;
        LcsItem matchDown = null;
        int d = 0;
        if (lcsInput.equals(a0, b0)) {
            for (d = 1; d < min && lcsInput.equals(a0 + d, b0 + d); d++) {}
            matchDown = seqGen.match(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        LcsItem matchUp = null;
        int u = 0;
        if (lcsInput.equals(a0 + n - 1, b0 + m - 1)) {
            final int x0 = a0 + n - 1;
            final int y0 = b0 + m - 1;
            final int maxu = min - d;
            for (u = 1; u < maxu && lcsInput.equals(x0 - u, y0 - u); u++) {}
            matchUp = seqGen.match(a0 + n - u, b0 + m - u, u);
        }
        LcsItem lcsMatch = null;
        if (u + d < min) {
            lcsMatch = lcs(lcsInput, seqGen, a0+d, n-d-u, b0+d, m-d-u);
        }
        return seqGen.chain(matchDown, lcsMatch, matchUp);
    }

}
