package com.fillumina.lcs;

/**
 * Implementation of the Linear Space Myers LCS algorithm. This is the base
 * class containing the algorithm. It only provides the indexes of the
 * LCS matches.
 *
 * @see AbstractLinearSpaceMyersLcSequence
 * @see LinearSpaceMyersLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs<T> extends LcsHeadTailReducer<T, int[][]> {
    public static final LinearSpaceMyersLcs<?> INSTANCE =
            new LinearSpaceMyersLcs<>();

    @SuppressWarnings("unchecked")
    public static <T> LinearSpaceMyersLcs<T> instance() {
        return (LinearSpaceMyersLcs<T>) INSTANCE;
    }

    /** Recursive linear space Myers algorithm. */
    @Override
    LcsItemImpl lcs(int[][] vv,
            final T[] a, final int a0, final int n,
            final T[] b, final int b0, final int m) {
        if (n == 1) {
            for (int i = b0; i < b0 + m; i++) {
                final T t = a[a0];
                if (same(t, b[i])) {
                    return (LcsItemImpl) match(a0, i, 1);
                }
            }
            return null;
        }
        if (m == 1) {
            for (int i = a0; i < a0 + n; i++) {
                final T t = b[b0];
                if (same(a[i], t)) {
                    return (LcsItemImpl) match(i, b0, 1);
                }
            }
            return null;
        }

        if (vv == null) {
            vv = new int[2][n+m+4];
        }

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
                            same(a[a0 + xEnd], b[b0 + yEnd])) {
                        xEnd++;
                        yEnd++;
                    }
                    vf[vIndex] = xEnd;
                    if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                            vb[vIndex - delta] <= xEnd) {
                        xStart = isPrev ? next : prev;
                        yStart = xStart - (k + (isPrev ? 1 : -1));
                        if (xEnd > xMid) {
                            match = (LcsItemImpl) match(a0 + xMid, b0 + (xMid - k),
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
                            same(a[a0+xStart-1], b[b0+yStart-1])) {
                        xStart--;
                        yStart--;
                    }
                    vb[vIndex] = xStart;
                    if (evenDelta && -d <= k && k <= d && xStart >= 0 &&
                            xStart <= vf[vIndex + delta]) {
                        xEnd = isPrev ? prev : next;
                        yEnd = xEnd - (k + (isPrev ? -1 : 1));
                        if (xMid > xStart) {
                            match = (LcsItemImpl) match(a0 + xStart, b0 + yStart,
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

        LcsItemImpl before = (LcsItemImpl) (fromStart ? null :
                lcsHeadTail(vv, a, a0, xStart, b, b0, yStart));

        LcsItemImpl after = (LcsItemImpl) (toEnd ? null :
                lcsHeadTail(vv, a, a0+xEnd, n-xEnd, b, b0+yEnd, m-yEnd));

        return LcsItemImpl.chain(before, match, after);
    }
}
