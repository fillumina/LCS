package com.fillumina.lcs;

/**
 * Implementation of the Linear Space Myers LCS algorithm. For maximum
 * flexibility its input is provided by extending the class.
 * It returns an ordered sequence of {@link LcsItem}s.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLinearSpaceMyersLcs {

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

    /** Perform the calculation. */
    public LcsItem calculateLcs() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        if (n == 0 || m == 0) {
            return LcsItem.NULL;
        }
        final LcsItem lcsItem = lcsTail(0, n, 0, m, null);
        return lcsItem == null ? LcsItem.NULL : lcsItem;
    }

    /**
     * Optimizes the calculation by filtering out equals elements on the head
     * and tail of the sequences.
     */
    private LcsItem lcsTail(final int a0, final int n,
            final int b0, final int m, final int[][] vv) {
        final int min = n < m ? n : m;
        LcsItem matchDown = null;
        LcsItem matchUp = null;
        LcsItem lcsMatch = null;
        int d = 0;
        if (equals(a0, b0)) {
            for (d = 1; d < min && equals(a0 + d, b0 + d); d++);
            matchDown = new LcsItem(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u = 0;
        if (equals(a0 + n - 1, b0 + m - 1)) {
            final int x0 = a0 + n - 1;
            final int y0 = b0 + m - 1;
            final int maxu = min - d;
            for (u = 1; u < maxu && equals(x0 - u, y0 - u); u++);
            matchUp = new LcsItem(a0 + n - u, b0 + m - u, u);
        }
        if (u + d < min) {
            lcsMatch = lcsRec(a0 + d, n - d - u, b0 + d, m - d - u, vv);
        }
        return LcsItem.chain(matchDown, lcsMatch, matchUp);
    }

    /** Recursive linear space Myers algorithm. */
    private LcsItem lcsRec(final int a0, final int n,
            final int b0, final int m, int[][] vv) {
        if (n == 1) {
            for (int i = b0; i < b0 + m; i++) {
                if (equals(a0, i)) {
                    return new LcsItem(a0, i, 1);
                }
            }
            return null;
        }
        if (m == 1) {
            for (int i = a0; i < a0 + n; i++) {
                if (equals(i, b0)) {
                    return new LcsItem(i, b0, 1);
                }
            }
            return null;
        }

        if (vv == null) {
            vv = new int[2][n+m+4];
        }

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

            final int max = (n + m + 1) >> 1;
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
                        if (xEnd > xMid) {
                            xStart = isPrev ? next : prev + 1;
                            yStart = xStart - (k + (isPrev ? 1 : -1));
                            match = new LcsItem(a0 + xMid, b0 + (xMid - k),
                                    xEnd - xMid);
                        } else {
                            xStart = isPrev ? next : prev;
                            yStart = xStart - (k + (isPrev ? 1 : -1));
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
                        if (xMid > xStart) {
                            xEnd = isPrev ? prev : next - 1;
                            yEnd = xEnd - (k + (isPrev ? -1 : 1));
                            match = new LcsItem(a0 + xStart, b0 + yStart,
                                    xMid - xStart);
                        } else {
                            xEnd = isPrev ? prev : next;
                            yEnd = xEnd - (k + (isPrev ? -1 : 1));
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
                lcsTail(a0, xStart, b0, yStart, vv);

        LcsItem after = toEnd ? null :
                lcsTail(a0 + xEnd, n - xEnd, b0 + yEnd, m - yEnd, vv);

        return LcsItem.chain(before, match, after);
    }

    /** *  Override if you need to control how {@link LcsItem} is created. */
    public LcsItem crateateLcsItem(int x, int y, int steps) {
        return new LcsItem(x, y, steps);
    }
}
