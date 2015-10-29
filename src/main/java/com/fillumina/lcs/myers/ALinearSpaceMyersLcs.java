package com.fillumina.lcs.myers;

import com.fillumina.lcs.Lcs;
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
    public List<T> lcs(final List<T> a, final List<T> b) {
        final int n = a.size();
        final int m = b.size();
        Match snakes = lcsTails(a, 0, n, b, 0, m);
        return extractLcs(snakes, a);
    }

    /** Recognizes equal heads and tails so to speed up the calculations. */
    public Match lcsTails(final List<T> a, final int a0, final int n,
            final List<T> b, final int b0, final int m) {
        final int min = Math.min(n, m);
        int d;
        for (d=0; d<min && a.get(a0+d).equals(b.get(b0+d)); d++);
        if (d != 0) {
            return Match.chain(new Match(a0, b0, d),
                    lcsTails(a, a0+d, n-d, b, b0+d, m-d));
        }
        int u;
        for (u=0; u<min && a.get(a0+n-u-1).equals(b.get(b0+m-u-1)); u++);
        if (u != 0) {
            return Match.chain(lcs(a, a0, n-u, b, b0, m-u, createEndpoint()),
                    new Match(a0+n-u, b0+m-u, u));
        }
        return lcs(a, a0, n, b, b0, m, createEndpoint());
    }

    public Match lcs(final List<T> a, final int a0, final int n,
            final List<T> b, final int b0, final int m,
            final int[] endpoint) {

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
                if (a.get(a0).equals(b.get(b0))) {
                    return diagonal(endpoint, a0, b0, 1);
                }
                return relativeBoundaries(endpoint, a0, b0, 1, 1);
            }

            relativeBoundaries(endpoint, a0, b0, 1, m);
            T t = a.get(a0);
            for (int i = b0; i < b0+m; i++) {
                if (t.equals(b.get(i))) {
                    return new Match(a0, i, 1);
                }
            }
            return Match.NULL;
        }

        if (m == 1) {
            relativeBoundaries(endpoint, a0, b0, n, 1);
            T t = b.get(b0);
            for (int i = a0; i < a0+n; i++) {
                if (t.equals(a.get(i))) {
                    return new Match(i, b0, 1);
                }
            }
            return Match.NULL;
        }

        final int[] middleSnakeEndpoint = createEndpoint();
        final Match diagonal = findMiddleSnake(a, a0, n, b, b0, m, middleSnakeEndpoint);
        final boolean fromStart = a0 == xStart(middleSnakeEndpoint);
        final boolean toEnd = xEnd(middleSnakeEndpoint) >= a0 + n;

        if (fromStart && toEnd) {
            relativeBoundaries(endpoint, a0, a0, n, m);
            return diagonal;
        }

        int[] beforeEndpoint = null, afterEndpoint = null;

        Match before = fromStart ?
                null :
                lcs(a, a0, xStart(middleSnakeEndpoint) - a0,
                        b, b0, yStart(middleSnakeEndpoint) - b0,
                        beforeEndpoint = createEndpoint());

        Match after = toEnd ?
                null :
                lcs(a, xEnd(middleSnakeEndpoint),
                        a0 + n - xEnd(middleSnakeEndpoint),
                        b, yEnd(middleSnakeEndpoint),
                        b0 + m - yEnd(middleSnakeEndpoint),
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

    Match findMiddleSnake(final List<T> a, final int a0, final int n,
            final List<T> b, final int b0, final int m,
            final int[] endpoint) {
        final int fullSize = n + m + 1;
        final int max = (fullSize >> 1) + 1; // ==> (fullSize / 2) + 1
        final int delta = n - m;
        final boolean oddDelta = (delta & 1) == 1; // delta is odd

        final int[][] vv = new int[2][fullSize+4]; // this can be externalized
        final int[] vf = vv[0];
        final int[] vb = vv[1];

        vb[max + delta - 1] = n;

        boolean isPrev, isVBounded;
        int kk, x, y, kStart, kEnd, prev, next, xStart, yStart, xMid, maxk;
        for (int d = 0; d < max; d++) {
            kStart = delta - (d - 1);
            kEnd = delta + (d - 1);
            for (int k = -d; k <= d; k += 2) {
                maxk = max + k;
                next = vf[maxk + 1];
                prev = vf[maxk - 1];
                isPrev = k == -d || (k != d && prev < next);
                if (isPrev) {
                    x = next;       // down
                } else {
                    x = prev + 1;   // right
                }

                xMid = x;
                y = x - k;
                while (x >= 0 && y >= 0 && x < n && y < m &&
                        a.get(a0+x).equals(b.get(b0+y))) {
                    x++;
                    y++;
                }
                vf[maxk] = x;

                if (oddDelta && isIn(k, kStart, kEnd) && x > 0 && vb[maxk] <= x) {
                    xStart = isPrev ? next : prev + 1;
                    yStart = xStart - (k + (isPrev ? 1 : -1));

                    absoluteBoundaries(endpoint, a0+xStart, b0+yStart, a0+x, b0+(x-k));
                    if (x > xMid) {
                        return new Match(a0+xMid, b0+(xMid-k), x-xMid);
                    } else {
                        return Match.NULL;
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;

                isVBounded = -max <= kk-1 && kk+1 <= max;

                maxk = max + kk;
                next = isVBounded ? vb[maxk + 1] : -1;
                prev = isVBounded ? vb[maxk - 1] : -1;
                isPrev = kk == d + delta || (kk != -d + delta && prev < next);
                if (isPrev) {
                    x = prev;   // up
                } else {
                    x = next - 1;   // left
                }

                xMid = x;
                y = x - kk;
                while (x > 0 && y > 0 && x <= n && y <= m &&
                        a.get(a0+x-1).equals(b.get(b0+y-1))) {
                    x--;
                    y--;
                }

                if (x >= 0 && -max < kk && kk < max) {
                    vb[maxk] = x;
                }

                if (!oddDelta && isIn(kk, -d, +d) && x >= 0 && x <= vf[maxk]) {
                    xStart = isPrev ? prev : next - 1;
                    yStart = xStart - (kk + (isPrev ? -1 : 1));

                    final int a0XEnd = a0 + x;
                    final int b0YEnd = b0 + (x - kk);

                    absoluteBoundaries(endpoint, a0XEnd, b0YEnd, a0+xStart, b0+yStart);
                    if (xMid > x) {
                        return new Match(a0XEnd, b0YEnd, xMid-x);
                    } else {
                        return Match.NULL;
                    }
                }
            }
        }
        return Match.NULL;
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
    private List<T> extractLcs(Match snakes, List<T> a) {
//        System.out.println("SOLUTION:");
        List<T> list = new ArrayList<>();
        for (Match segment : snakes) {
//            System.out.println(segment.toString());
            segment.addEquals(list, a);
        }
        return list;
    }

    public static class Match implements Iterable<Match>, Serializable {
        private static final long serialVersionUID = 1L;
        public static final Match NULL = new Match(-1, -1, -1);
        private final int x, y, steps;
        private Match next;
        private Match last;

        private Match(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSteps() {
            return steps;
        }

        private <T> void addEquals(List<T> list, List<T> a) {
            for (int i = x; i < x + steps; i++) {
                list.add(a.get(i));
            }
        }

        private static Match chain(Match... segments) {
            switch (segments.length) {
                case 0: return NULL;
                case 1: return segments[1];
            }
            Match head = NULL;
            Match current = null;
            for (Match s : segments) {
                if (s != null && s != NULL) {
                    if (head == NULL) {
                        current = head = s;
                    } else {
                        current.next = s;
                    }
                    while (current.last != null) {
                        current = current.last;
                    }
                    while (current.next != null) {
                        current = current.next;
                    }
                }
            }
            if (current != head) {
                head.last = current;
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
                    "{xStart=" + x + ", yStart=" + y + ", steps=" + steps + '}';
        }
    }

    // methods to manage endpoints and snake in a slightly more meaningfully way
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
}
