package com.fillumina.lcs;

/**
 * Implementation of the Linear Space Myers LCS algorithm. For maximum
 * flexibility its input is provided by extending the class.
 * It returns an ordered sequence of {@link LcsItemImpl}s.
 * Note that this class can be used for one calculation only.
 *
 * @see LinearSpaceMyersLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLinearSpaceMyersLcIndex {
    private int[][] vv;
    protected LcsItem head;

    /** Override to return the length of the first sequence. */
    protected abstract int getFirstSequenceLength();

    /** Override to return the length of the second sequence. */
    protected abstract int getSecondSequenceLength();

    /**
     * @param x the index in the first sequence
     * @param y the index in the second sequence
     * @return {@code true} if the item with index x on the first sequence
     *          is equal to the item with index y on the second sequence.
     */
    protected abstract boolean equals(int x, int y);

    protected abstract LcsItem match(int x, int y, int steps);

    /** Perform the calculation. */
    @SuppressWarnings("unchecked")
    public void calculateLcs() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        if (n == 0 || m == 0) {
            head = (LcsItemImpl) LcsItemImpl.NULL;
            return;
        }
        final LcsItem lcsItem = lcsTail(0, n, 0, m);
        head = (LcsItemImpl) (lcsItem == null ? LcsItemImpl.NULL : lcsItem);
    }

    /**
     * Optimizes the calculation by filtering out equal elements on the head
     * and tail of the sequences.
     */
    private LcsItem lcsTail(final int a0, final int n,
            final int b0, final int m) {
        final int min = n < m ? n : m;
        LcsItem matchDown = null;
        int d = 0;
        if (equals(a0, b0)) {
            for (d = 1; d < min && equals(a0 + d, b0 + d); d++);
            matchDown = new LcsItemImpl(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        LcsItem matchUp = null;
        int u = 0;
        if (equals(a0 + n - 1, b0 + m - 1)) {
            final int x0 = a0 + n - 1;
            final int y0 = b0 + m - 1;
            final int maxu = min - d;
            for (u = 1; u < maxu && equals(x0 - u, y0 - u); u++);
            matchUp = new LcsItemImpl(a0 + n - u, b0 + m - u, u);
        }
        LcsItem lcsMatch = null;
        if (u + d < min) {
            lcsMatch = lcsRec(a0 + d, n - d - u, b0 + d, m - d - u);
        }
        return chain(matchDown, lcsMatch, matchUp);
    }

    protected LcsItem chain(final LcsItem before,
            final LcsItem middle, final LcsItem after) {
        return null;
    }

    /** Recursive linear space Myers algorithm. */
    private LcsItem lcsRec(final int a0, final int n,
            final int b0, final int m) {
        if (n == 1) {
            for (int i = b0; i < b0 + m; i++) {
                if (equals(a0, i)) {
                    return new LcsItemImpl(a0, i, 1);
                }
            }
            return null;
        }
        if (m == 1) {
            for (int i = a0; i < a0 + n; i++) {
                if (equals(i, b0)) {
                    return new LcsItemImpl(i, b0, 1);
                }
            }
            return null;
        }

        if (this.vv == null) {
            this.vv = new int[2][n+m+4];
        }
        // make a local variable which is faster to access
        final int[][] vv = this.vv;

        LcsItem match = null;
        int xStart = -1;
        int yStart = -1;
        int xEnd = -1;
        int yEnd = -1;
        {
            // the find_middle_snake() function is embedded into the main
            // function so to avoid a method call and to have easy access
            // to all the required parameters. It uses a different scope
            // to have less garbage on the stack when recursing.

            final int delta = n - m;
            final boolean evenDelta = (delta & 1) == 0;
            final int[] vf = vv[0];
            final int[] vb = vv[1];
            final int halfv = vf.length >> 1;

            vf[halfv + 1] = 0;
            vf[halfv + delta] = 0;
            vb[halfv - 1] = n;
            vb[halfv - delta] = n;

            boolean isPrev;
            int k;
            int prev;
            int next;
            int vIndex;
            int xMid;
            int kStart = delta - 1;
            int kEnd = delta + 1;

            final int max = (n + m + 1) >> 1;

            FIND_MIDDLE_SNAKE:
            for (int d = 0; d <= max; d++) {
                // forward Myers algorithm
                if (d != 0) {
                    kStart = delta - (d - 1);
                    kEnd = delta + (d - 1);
                }
                for (k = -d; k <= d; k += 2) {
                    vIndex = halfv + k;
                    next = vf[vIndex + 1];
                    prev = vf[vIndex - 1];
                    isPrev = k == -d || (k != d && prev < next);
                    if (isPrev) {
                        xEnd = next; // down
                    } else {
                        xEnd = prev + 1; // right
                    }
                    yEnd = xEnd - k;
                    xMid = xEnd;
                    while (xEnd < n && yEnd < m &&
                            equals(a0 + xEnd, b0 + yEnd)) {
                        xEnd++;
                        yEnd++;
                    }
                    vf[vIndex] = xEnd;
                    if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                            vb[vIndex - delta] <= xEnd) {
                        xStart = isPrev ? next : prev;
                        yStart = xStart - (k + (isPrev ? 1 : -1));
                        if (xEnd > xMid) {
                            match = match(a0 + xMid, b0 + (xMid - k),
                                    xEnd - xMid);
                        }
                        break FIND_MIDDLE_SNAKE;
                    }
                }

                // reverse Myers algorithm
                kStart = delta - d;
                kEnd = delta + d;
                for (k = kStart; k <= kEnd; k += 2) {
                    vIndex = halfv + k - delta;

                    next = vb[vIndex + 1];
                    prev = vb[vIndex - 1];
                    isPrev = k == kEnd || (k != kStart && prev < next);
                    if (isPrev) {
                        xStart = prev; // up
                    } else {
                        xStart = next - 1; // left
                    }
                    yStart = xStart - k;
                    xMid = xStart;
                    while (xStart > 0 && yStart > 0 &&
                            equals(a0 + xStart - 1, b0 + yStart - 1)) {
                        xStart--;
                        yStart--;
                    }
                    vb[vIndex] = xStart;
                    if (evenDelta && -d <= k && k <= d && xStart >= 0 &&
                            xStart <= vf[vIndex + delta]) {
                        xEnd = isPrev ? prev : next;
                        yEnd = xEnd - (k + (isPrev ? -1 : 1));
                        if (xMid > xStart) {
                            match = match(a0 + xStart, b0 + yStart,
                                    xMid - xStart);
                        }
                        break FIND_MIDDLE_SNAKE;
                    }
                }
            }
        }
        final boolean fromStart = xStart <= 0 || yStart <= 0;
        final boolean toEnd = xEnd >= n || n - xEnd == 0 || m - yEnd == 0;
        if (fromStart && toEnd) {
            return match;
        }
        LcsItem before = fromStart ? null :
                lcsTail(a0, xStart, b0, yStart);

        LcsItem after = toEnd ? null :
                lcsTail(a0 + xEnd, n - xEnd, b0 + yEnd, m - yEnd);

        return chain(before, match, after);
    }
}
