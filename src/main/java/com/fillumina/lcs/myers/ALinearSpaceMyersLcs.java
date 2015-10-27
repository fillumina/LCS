package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.VList;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.util.BidirectionalVector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The indexes are passed along the calls so to avoid using sublists.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ALinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        final VList<T> va = new VList<>(a);
        final VList<T> vb = new VList<>(b);
        Match snakes = lcs(va, 0, va.size(),
                vb, 0, vb.size(),
                createEndpoint());
        return extractLcs(snakes, va);
    }

    Match lcs(VList<T> a, int a0, int n,
            VList<T> b, int b0, int m,
            int[] endpoint) {
//        final int n = aEnd - a0;
//        final int m = bEnd - b0;

        if (a0 != a.zero() || b0 != b.zero()) {
            throw new AssertionError();
        }

        if (n != a.size() || m != b.size()) {
            throw new AssertionError();
        }

        assert a0 == a.zero();
        assert b0 == b.zero();

        if (n == 0) {
            if (m != 0) {
                return vertical(endpoint, a0, b0, m);
            }
            return Match.NULL;
        }

        if (m == 0 /* && n != 0 */) {
            return horizontal(endpoint, a0, b0, n);
        }

        if (n == 1) {
            if (m == 1) {
                if (a.get(1).equals(b.get(1))) {
                    return diagonal(endpoint, a0, b0, 1);
                }
                return relativeBoundaries(endpoint, a0, b0, 1, 1);
            }

            relativeBoundaries(endpoint, a0, a0, 1, m);
            T t = a.get(1);
            for (int i = 1; i <= m; i++) {
                if (t.equals(b.get(i))) {
                    if (i == 1) {
                        return new Match(a0, b0, 1);
                    } else if (i == m) {
                        return new Match(a0, b0+m-1, 1);
                    } else {
                        return new Match(a0, b0-i-1, 1);
                    }
                }
            }
            return Match.NULL;
        }

        if (m == 1) {
            relativeBoundaries(endpoint, a0, a0, n, 1);
            T t = b.get(1);
            for (int i = 1; i <= n; i++) {
                if (t.equals(a.get(i))) {
                    if (i == 1) {
                        return new Match(a0, b0, 1);
                    } else if (i == n) {
                        return new Match(a0+n-1, b0, 1);
                    }
                    return new Match(a0+i-1, b0, 1);
                }
            }
            return Match.NULL;
        }

        final int[] middleSnakeEndpoint = createEndpoint();
        final Match diagonal = findMiddleSnake(a, n, b, m, middleSnakeEndpoint);
        final boolean fromStart = touchingStart(a0, b0, middleSnakeEndpoint);
        final boolean toEnd = touchingEnd(a0+n, b0+m, middleSnakeEndpoint);

        if (fromStart && toEnd) {
            relativeBoundaries(endpoint, a0, a0, n, m);
            return diagonal;
        }

        int[] beforeEndpoint = null, afterEndpoint = null;

        Match before = fromStart ?
                Match.NULL :
                lcs(a.subList(1, xStart(middleSnakeEndpoint) + 1 - a0),
                        a0, xStart(middleSnakeEndpoint) - a0,
                        b.subList(1, yStart(middleSnakeEndpoint) + 1 - b0),
                        b0, yStart(middleSnakeEndpoint) - b0,
                        beforeEndpoint = createEndpoint());

        Match after = toEnd ?
                Match.NULL :
                lcs(a.subList(xEnd(middleSnakeEndpoint) + 1 - a0, n + 1),
                        xEnd(middleSnakeEndpoint),
                        a0 + n - (xEnd(middleSnakeEndpoint)),
                        b.subList(yEnd(middleSnakeEndpoint) + 1 - b0, m + 1),
                        yEnd(middleSnakeEndpoint),
                        b0 + m - (yEnd(middleSnakeEndpoint)),
                        afterEndpoint = createEndpoint());

        if (fromStart) {
            absoluteBoundaries(endpoint,
                    xStart(middleSnakeEndpoint), yStart(middleSnakeEndpoint),
                    xEnd(afterEndpoint), yEnd(afterEndpoint));
            return Match.chain(diagonal, after);
        } else if (toEnd) {
            absoluteBoundaries(endpoint,
                    xStart(beforeEndpoint), yStart(beforeEndpoint),
                    xEnd(middleSnakeEndpoint), yEnd(middleSnakeEndpoint));
            return Match.chain(before, diagonal);
        }
        absoluteBoundaries(endpoint,
                xStart(beforeEndpoint), yStart(beforeEndpoint),
                xEnd(afterEndpoint), yEnd(afterEndpoint));
        return Match.chain(before, diagonal, after);
    }

    Match findMiddleSnake(VList<T> a, int n, VList<T> b, int m,
            int[] endpoint) {
        final int max = (n + m + 1) / 2;
        final int delta = n - m;
        final boolean oddDelta = (delta & 1) == 1;

        // TODO find a way to allocate those only once and reuse them indexed
        final BidirectionalVector vf = new BidirectionalVector(max + 1);
        final BidirectionalVector vb = new BidirectionalVector(max + 1);

        vb.set(delta - 1, n);

        int kk, xf, xr, start, end;
        for (int d = 0; d <= max; d++) {
            start = delta - (d - 1);
            end = delta + (d - 1);
            for (int k = -d; k <= d; k += 2) {
                xf = findFurthestReachingDPath(d, k, a, n, b, m, vf);
                if (oddDelta && isIn(k, start, end)) {
                    if (xf > 0 && vb.get(k) <= xf) {
                        return findLastSnake(d, k, xf, a.zero(), b.zero(), vf, a,
                                b, endpoint);
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = findFurthestReachingDPathReverse(d, kk, a, n, b, m, delta,
                        vb);
                if (!oddDelta && isIn(k, -d, +d)) {
                    if (xr >= 0 && xr <= vf.get(kk)) {
                        return findLastSnakeReverse(d, kk, xr, a.zero(), b.
                                zero(), vb, delta, a, n, b, m, endpoint);
                    }
                }
            }
        }
        return Match.NULL;
    }

    // TODO inline those calls?
    private int findFurthestReachingDPath(int d, int k,
            VList<T> a, int n, VList<T> b, int m,
            BidirectionalVector v) {
        int x, y;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        if (k == -d || (k != d && prev < next)) {
            x = next;
        } else {
            x = prev + 1;
        }
        y = x - k;
        while (x >= 0 && y >= 0 && x < n && y < m &&
                a.get(x + 1).equals(b.get(y + 1))) {
            x++;
            y++;
        }
        v.set(k, x);
        return x;
    }

    private Match findLastSnake(int d, int k, int x, int x0, int y0,
            BidirectionalVector v,
            VList<T> a, VList<T> b,
            int[] endpoint) {
        int y = x - k;

        int xEnd = x;
        int yEnd = y;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        int xStart, yStart, xMid, yMid;
        if (k == -d || (k != d && prev < next)) {
            xStart = next;
            yStart = next - k - 1;
            xMid = xStart;
        } else {
            xStart = prev;
            yStart = prev - k + 1;
            xMid = xStart + 1;
        }

        yMid = xMid - k;

        boolean reverse = false;

        if (xMid == xEnd && yMid == yEnd) {
            xMid = xStart;
            yMid = yStart;
            while (xStart > 0 && yStart > 0 && a.get(xStart).equals(b.
                    get(yStart))) {
                xStart--;
                yStart--;
            }
            reverse = true;
        }

        absoluteBoundaries(endpoint, x0+xStart, y0+yStart, x0+xEnd, y0+yEnd);
        if (reverse) {
            return Match.create(x0+xStart, y0+yStart, xMid-xStart);
        }
        return Match.create(x0+xMid, y0+yMid, xEnd-xMid);
    }

    private int findFurthestReachingDPathReverse(int d, int k,
            VList<T> a, int n, VList<T> b, int m,
            int delta, BidirectionalVector v) {
        int x, y;

        final int next = v.get(k + 1); // left
        final int prev = v.get(k - 1); // up
        if (k == d + delta || (k != -d + delta && prev < next)) {
            x = prev;   // up
        } else {
            x = next - 1;   // left
        }
        y = x - k;
        while (x > 0 && y > 0 && x <= n && y <= m &&
                a.get(x).equals(b.get(y))) {
            x--;
            y--;
        }
        if (x >= 0) {
            v.set(k, x);
        }
        return x;
    }

    private Match findLastSnakeReverse(int d, int k, int xe, int x0, int y0,
            BidirectionalVector v, int delta,
            VList<T> a, int n, VList<T> b, int m,
            int[] endpoint) {
        final int xStart = xe;
        final int yStart = xe - k;
        int xEnd, yEnd, xMid, yMid;

        final int next = v.get(k + 1);
        final int prev = v.get(k - 1);
        if (k == d + delta || (k != -d + delta && prev != 0 && prev < next)) {
            xEnd = prev;
            yEnd = prev - k + 1;
            xMid = xEnd;
        } else {
            xEnd = next;
            yEnd = next - k - 1;
            xMid = xEnd - 1;
        }
        yMid = xMid - k;

        boolean reverse = true;
        if (xMid == xStart && yMid == yStart) {
            xMid = xEnd;
            yMid = yEnd;
            while (xEnd < n && yEnd < m && a.get(xEnd + 1).equals(b.
                    get(yEnd + 1))) {
                xEnd++;
                yEnd++;
            }
            reverse = false;
        }

        absoluteBoundaries(endpoint, x0+xStart, y0+yStart, x0+xEnd, y0+yEnd);
        if (reverse) {
            return Match.create(x0+xStart, y0+yStart, xMid-xStart);
        }
        return Match.create(x0+xMid, y0+yMid, xEnd-xMid);
    }

    private static boolean isIn(int value, int startInterval, int endInterval) {
        if (startInterval < endInterval) {
            if (value < startInterval) {
                return false;
            }
            if (value > endInterval) {
                return false;
            }
        } else {
            if (value > startInterval) {
                return false;
            }
            if (value < endInterval) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the common subsequence elements.
     */
    private List<T> extractLcs(Match snakes, VList<T> a) {
        System.out.println("SOLUTION:");
        List<T> list = new ArrayList<>();
        for (Match segment : snakes) {
            System.out.println(segment.toString());
            segment.addEquals(list, a);
        }
        return list;
    }

    public static class Match implements Iterable<Match>, Serializable {
        private static final long serialVersionUID = 1L;
        public static final Match NULL = new Match(-1, -1, -1);
        private final int x, y, steps;
        private Match next;

        private static Match create(int x, int y, int steps) {
            if (steps <= 0) {
                return Match.NULL;
            }
            return new Match(x, y, steps);
        }

        private Match(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }

        @Deprecated
        <T> void addEquals(List<T> list, VList<T> a) {
            for (int i = x + 1; i <= x + steps; i++) {
                list.add(a.get(i));
            }
        }

        static Match chain(Match... segments) {
            Match head = NULL;
            Match current = null;
            for (Match s : segments) {
                if (s != null && s != NULL) {
                    if (head == NULL) {
                        current = head = s;
                    } else {
                        current.next = s;
                    }
                    while (current.next != null) {
                        current = current.next;
                    }
                }
            }
            return head;
        }

        @Override
        public Iterator<Match> iterator() {
            return new Iterator<Match>() {
                private Match current = Match.this;

                @Override
                public boolean hasNext() {
                    return current != null && current != NULL;
                }

                @Override
                public Match next() {
                    Match tmp = current;
                    current = current.next;
                    return tmp;
                }
            };
        }

        static String toString(Match s) {
            Match current = s;
            StringBuilder buf = new StringBuilder("[");
            buf.append(s.toString());
            while (current.next != null && current.next != NULL) {
                current = current.next;
                buf.append(", ").append(current.toString());
            }
            buf.append("]");
            return buf.toString();
        }

        @Override
        public String toString() {
            if (this == NULL) {
                return "Match{NULL}";
            }
            return getClass().getSimpleName() +
                    "{ steps=" + steps +
                    ", xStart=" + x + ", yStart=" + '}';
        }
    }

    // methods to manage endpoints in a slightly more meaningful way
    // (an object would have been a performance overkill)

    private static int xStart(int[] endpoint) {
        return endpoint[0];
    }

    private static int yStart(int[] endpoint) {
        return endpoint[1];
    }

    private static int xEnd(int[] endpoint) {
        return endpoint[2];
    }

    private static int yEnd(int[] endpoint) {
        return endpoint[3];
    }

    private static int[] createEndpoint() {
        return new int[4];
    }

    private static Match relativeBoundaries(int[] endpoint,
            int xStart, int yStart, int xStep, int yStep) {
        endpoint[0] = xStart;
        endpoint[1] = yStart;
        endpoint[2] = xStart + xStep;
        endpoint[3] = yStart + yStep;
        return Match.NULL;
    }

    private static Match absoluteBoundaries(int[] endpoint,
            int xStart, int yStart, int xEnd, int yEnd) {
        endpoint[0] = xStart;
        endpoint[1] = yStart;
        endpoint[2] = xEnd;
        endpoint[3] = yEnd;
        return Match.NULL;
    }

    private static Match diagonal(int[] endpoint,
            int xStart, int yStart, int step) {
        endpoint[0] = xStart;
        endpoint[1] = yStart;
        endpoint[2] = xStart + step;
        endpoint[3] = yStart + step;
        return new Match(xStart, yStart, step);
    }

    private static Match horizontal(int[] endpoint,
            int xStart, int yStart, int step) {
        endpoint[0] = xStart;
        endpoint[1] = yStart;
        endpoint[2] = xStart + step;
        endpoint[3] = yStart;
        return Match.NULL;
    }

    private static Match vertical(int[] endpoint,
            int xStart, int yStart, int step) {
        endpoint[0] = xStart;
        endpoint[1] = yStart;
        endpoint[2] = xStart;
        endpoint[3] = yStart + step;
        return Match.NULL;
    }

    private static boolean touchingStart(int x, int y, int[] endpoint) {
        return x == xStart(endpoint) && y == yStart(endpoint);
    }

    private static boolean touchingEnd(int x, int y, int[] endpoint) {
        return xEnd(endpoint) >= x && yEnd(endpoint) >= y;
    }
}
