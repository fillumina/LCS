package com.fillumina.lcs.myers;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
// TODO add management of very long sequences
// TODO apply the maximum speedup!
public abstract class LinearSpaceMyersLcs implements Lcs {

    @Override
    public Match getMatch() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        return lcsTail(0, n, 0, m, new int[2][3 *(n+m+1)]);
    }

    private Match lcsTail(final int a0, final int n, final int b0, final int m,
            int[][] vv) {
        final int min = n < m ? n : m;
        Match matchDown = null;
        Match matchUp = null;
        Match lcsMatch = null;
        int d;
        for (d = 0; d < min && equals(a0+d, b0+d); d++) {
            ;
        }
        if (d != 0) {
            matchDown = new LinearLcsMatch(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u;
        int x0 = a0 + n - 1;
        int y0 = b0 + m - 1;
        for (u = 0; u < min && equals(x0 - u, y0 - u); u++) {
            ;
        }
        if (u != 0) {
            matchUp = new LinearLcsMatch(a0 + n - u, b0 + m - u, u);
        }
        if (u + d != min) {
            lcsMatch = lcsRec(a0 + d, n - d - u, b0 + d, m - d - u, vv);
        }
        return Match.chain(matchDown, lcsMatch, matchUp);
    }

    private Match lcsRec(final int a0, final int n, final int b0, final int m,
            int[][] vv) {
        if (n == 0 || m == 0) {
            return null;
        }
        if (n == 1) {
            if (m == 1) {
                if (equals(a0, b0)) {
                    return new LinearLcsMatch(a0, b0, 1);
                }
                return null;
            }
            for (int i = b0; i < b0 + m; i++) {
                if (equals(a0, i)) {
                    return new LinearLcsMatch(a0, i, 1);
                }
            }
            return null;
        }
        if (m == 1) {
            for (int i = a0; i < a0 + n; i++) {
                if (equals(i, b0)) {
                    return new LinearLcsMatch(i, b0, 1);
                }
            }
            return null;
        }
        LinearLcsMatch match = null;
        int xStart = -1;
        int yStart = -1;
        int xEnd = -1;
        int yEnd = -1;
        // find middle snake
        {
            // set variables out of scope so to have less garbage on the stack
            final int max = (n + m + 1) / 2 + 1;
            final int delta = n - m;
            final boolean evenDelta = (delta & 1) == 0;
            final int[] vf = vv[0];
            final int[] vb = vv[1];
            final int halfv = vf.length / 2;

            vf[halfv + 1] = 0;
            vb[halfv + delta - 1] = n;
            vb[halfv - delta - 1] = n;

            boolean isPrev;
            int k;
            int kDeltaStart;
            int kDeltaEnd;
            int prev;
            int next;
            int maxk;
            int xMid;
            int kStart = delta - 1;
            int kEnd = delta + 1;

            FIND_MIDDLE_SNAKE:
            for (int d = 0; d <= max; d++) {
                if (d > 1) {
                    kStart = delta - (d - 1);
                    kEnd = delta + (d - 1);
                }
                for (k = -d; k <= d; k += 2) {
                    maxk = halfv + k;
                    next = vf[maxk + 1];
                    prev = vf[maxk - 1];
                    isPrev = k == -d || (k != d && prev < next);
                    if (isPrev) {
                        xEnd = next; // down
                    } else {
                        xEnd = prev + 1; // right
                    }
                    yEnd = xEnd - k;
                    xMid = xEnd;
                    while (xEnd < n && yEnd < m && equals(a0 + xEnd, b0 + yEnd)) {
                        xEnd++;
                        yEnd++;
                    }
                    vf[maxk] = xEnd;
                    if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                            vb[maxk] <= xEnd) {
                        if (xEnd > xMid) {
                            xStart = isPrev ? next : prev + 1;
                            yStart = xStart - (k + (isPrev ? 1 : -1));
                            match = new LinearLcsMatch(a0 + xMid, b0 + (xMid - k),
                                    xEnd - xMid);
                        } else {
                            xStart = isPrev ? next : prev;
                            yStart = xStart - (k + (isPrev ? 1 : -1));
                        }
                        break FIND_MIDDLE_SNAKE;
                    }
                }
                kDeltaEnd = delta + d;
                kDeltaStart = delta - d;
                for (k = kDeltaStart; k <= kDeltaEnd; k += 2) {
                    maxk = halfv + k;
                    next = vb[maxk + 1];
                    prev = vb[maxk - 1];
                    isPrev = k == kDeltaEnd || (k != kDeltaStart && prev < next);
                    if (isPrev) {
                        xStart = prev; // up
                    } else {
                        xStart = next - 1; // left
                    }
                    yStart = xStart - k;
                    xMid = xStart;
                    while (xStart > 0 && yStart > 0 && equals(a0 + xStart - 1,
                            b0 + yStart - 1)) {
                        xStart--;
                        yStart--;
                    }
                    vb[maxk] = xStart;
                    if (evenDelta && -d <= k && k <= d && xStart >= 0 &&
                            xStart <= vf[maxk]) {
                        if (xMid > xStart) {
                            xEnd = isPrev ? prev : next - 1;
                            yEnd = xEnd - (k + (isPrev ? -1 : 1));
                            match = new LinearLcsMatch(a0 + xStart, b0 + yStart,
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
        final boolean fromStart = xStart <= 0;
        final boolean toEnd = xEnd >= n;
        if (fromStart && toEnd) {
            return match;
        }
        Match before = fromStart ? null : lcsTail(a0, xStart, b0, yStart, vv);
        Match after = toEnd || n - xEnd == 0 || m - yEnd == 0 ? null
                : lcsTail(a0 + xEnd, n - xEnd, b0 + yEnd, m - yEnd, vv);

        return Match.chain(before, match, after);
    }

    //TODO copy the full Match here
    private static class LinearLcsMatch extends Match {
        private static final long serialVersionUID = 1L;
        private int lcs;

        private LinearLcsMatch(int x, int y, int steps) {
            super(x, y, steps);
            this.lcs = steps;
        }

        /**
         * <b>NOTE</b> that this value is accurate only for the first
         * element of the iterable.
         */
        @Override
        public int getLcs() {
            return lcs;
        }

        @Override
        protected void accumulateLcs(int otherLcs) {
            lcs += otherLcs;
        }
    }
}
