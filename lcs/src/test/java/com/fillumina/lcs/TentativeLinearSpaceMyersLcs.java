package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class TentativeLinearSpaceMyersLcs implements LcsList {

    @Override
    public <T> List<? extends T> lcs(final List<? extends T> a,
            final List<? extends T> b) {
        final int n = a.size();
        final int m = b.size();
        @SuppressWarnings("unchecked")
        final T[] arrayA = (T[])a.toArray(new Object[n]);
        @SuppressWarnings("unchecked")
        final LcsItemImpl matches = lcsTails(arrayA, n,
                (T[])b.toArray(new Object[m]), m);
        return extractLcsList(matches, arrayA);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> extractLcsList(List<LcsItem> lcsItem, T[] a) {
        final List<T> lcs = new ArrayList<>(lcsItem.size());
        for (int index : lcsItem.get(0).lcsIndexesOfTheFirstSequence()) {
            lcs.add(a[index]);
        }
        return lcs;
    }

    /** Recognizes equals head and tail so to speed up the calculations. */
    private <T> LcsItemImpl lcsTails(final T[] a, final int n,
            final T[] b, final int m) {
        final int min = n < m ? n : m;
        LcsItemImpl matchDown = null;
        LcsItemImpl matchUp = null, lcsMatch = null;
        int d;
        for (d=0; d<min && Objects.equals(a[d],b[d]); d++) {}
        if (d != 0) {
            matchDown = new LcsItemImpl(0, 0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u, x0=n-1, y0=m-1;
        for (u=0; u<min && Objects.equals(a[x0-u],b[y0-u]); u++) {}
        if (u != 0) {
            matchUp = new LcsItemImpl(n-u, m-u, u);
        }

        if (u + d != min) {
            lcsMatch = lcsRec(a, d, n-d-u, b, d, m-d-u,
                    new int[2][(n + m + 1) << 1]);
        }

        return chain(matchDown, lcsMatch, matchUp);
    }

    protected <T> LcsItemImpl lcsRec(final T[] a, final int a0, final int n,
            final T[] b, final int b0, final int m, int[][]vv) {

        if (n == 0 || m == 0) {
            return null;
        }

        if (n == 1) {
            if (m == 1) {
                if (Objects.equals(a[a0],b[b0])) {
                    return new LcsItemImpl(a0, b0, 1);
                }
                return null;
            }

            T t = a[a0];
            for (int i = b0; i < b0+m; i++) {
                if (Objects.equals(t,b[i])) {
                    return new LcsItemImpl(a0, i, 1);
                }
            }
            return null;
        }

        if (m == 1) {
            T t = b[b0];
            for (int i = a0; i < a0+n; i++) {
                if (Objects.equals(t,a[i])) {
                    return new LcsItemImpl(i, b0, 1);
                }
            }
            return null;
        }

        LcsItemImpl match = null;
        int xStart=-1, yStart=-1, xEnd=-1, yEnd=-1;

        // find middle snake
        { // set variables out of scope so to have less garbage on the stack
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
            int k, kDeltaStart, kDeltaEnd, prev, next, maxk, xMid;
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
                        xEnd = next;       // down
                    } else {
                        xEnd = prev + 1;   // right
                    }
                    yEnd = xEnd - k;

                    xMid = xEnd;
                    while (xEnd < n && yEnd < m &&
                            Objects.equals(a[a0+xEnd],b[b0+yEnd])) {
                        xEnd++;
                        yEnd++;
                    }
                    vf[maxk] = xEnd;

                    if (!evenDelta && kStart <= k && k <= kEnd &&
                                xEnd >=0 && vb[maxk] <= xEnd) {
                        if (xEnd > xMid) {
                            xStart = isPrev ? next : prev + 1;
                            yStart = xStart - (k + (isPrev ? 1 : -1));
                            match = new LcsItemImpl(a0+xMid, b0+(xMid-k), xEnd-xMid);
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
                        xStart = prev;   // up
                    } else {
                        xStart = next - 1;   // left
                    }
                    yStart = xStart - k;

                    xMid = xStart;
                    while (xStart > 0 && yStart > 0 &&
                            Objects.equals(a[a0+xStart-1],b[b0+yStart-1])) {
                        xStart--;
                        yStart--;
                    }
                    vb[maxk] = xStart;

                    if (evenDelta && -d <= k && k <= d &&
                        xStart >= 0 && xStart <= vf[maxk]) {

                        if (xMid > xStart) {
                            xEnd = isPrev ? prev : next - 1;
                            yEnd = xEnd - (k + (isPrev ? -1 : 1));
                            match = new LcsItemImpl(a0+xStart, b0+yStart, xMid-xStart);
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

        LcsItemImpl before = fromStart ? null :
                lcsRec(a, a0, xStart, b, b0, yStart, vv);

        LcsItemImpl after = toEnd || n-xEnd == 0 || m-yEnd == 0 ? null :
                lcsRec(a, a0+xEnd, n-xEnd, b, b0+yEnd, m-yEnd, vv);

        return chain(before, match, after);
    }

    private static LcsItemImpl chain(LcsItemImpl before, LcsItemImpl middle, LcsItemImpl after) {
        if (middle == null) {
            if (after == null) {
                return before;
            }
            if (before == null) {
                return after;
            }
            return before.chain(after);
        }
        if (after == null) {
            if (before == null) {
                return middle;
            }
            return before.chain(middle);
        }
        if (before == null) {
            return middle.chain(after);
        }
        return before.chain(middle.chain(after));
    }
}
