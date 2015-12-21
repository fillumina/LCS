package com.fillumina.lcs;

/**
 * Implementation of the Linear Space Myers LCS algorithm. It is fast
 * and memory efficient (O(n)).
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLinearSpaceMyersLcs
        extends AbstractLcsHeadTailReducer {
    private int[][] vv;

    public AbstractLinearSpaceMyersLcs() {
        super();
    }

    /**
     * @param sizeOnly {@code true} if only interested in the size of the LCS
     *                (the lists returned will be empty).
     */
    public AbstractLinearSpaceMyersLcs(boolean sizeOnly) {
        super(sizeOnly);
    }

    /** Recursive linear space Myers algorithm. */
    @Override
    LcsItemImpl lcs(final int a0, final int n, final int b0, final int m) {
        if (n == 1) {
            for (int i = b0; i < b0 + m; i++) {
                if (sameAtIndex(a0, i)) {
                    return match(a0, i, 1);
                }
            }
            return null;
        }
        if (m == 1) {
            for (int i = a0; i < a0 + n; i++) {
                if (sameAtIndex(i, b0)) {
                    return match(i, b0, 1);
                }
            }
            return null;
        }

        if (vv == null) {
            vv = new int[2][n+m+4];
        }
        int[][] vv = this.vv;

        LcsItemImpl match = null;
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
                            sameAtIndex(a0 + xEnd, b0 + yEnd)) {
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
                            sameAtIndex(a0+xStart-1, b0+yStart-1)) {
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

        LcsItemImpl before = fromStart ? null :
                lcsHeadTail(a0, xStart, b0, yStart);

        LcsItemImpl after = toEnd ? null :
                lcsHeadTail(a0+xEnd, n-xEnd, b0+yEnd, m-yEnd);

        return LcsItemImpl.chain(before, match, after);
    }
}
