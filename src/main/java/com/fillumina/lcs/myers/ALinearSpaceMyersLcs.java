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
        int u, x0=a0+n-1, y0=b0+m-1;
        for (u=0; u<min && a.get(x0-u).equals(b.get(y0-u)); u++);
        if (u != 0) {
            return Match.chain(lcs(a, a0, n-u, b, b0, m-u),
                    new Match(a0+n-u, b0+m-u, u));
        }
        return lcs(a, a0, n, b, b0, m);
    }

    public Match lcs(final List<T> a, final int a0, final int n,
            final List<T> b, final int b0, final int m) {

        if (n == 0 || m == 0) {
            return Match.NULL;
        }

        if (n == 1) {
            if (m == 1) {
                if (a.get(a0).equals(b.get(b0))) {
                    return new Match(a0, b0, 1);
                }
                return Match.NULL;
            }

            T t = a.get(a0);
            for (int i = b0; i < b0+m; i++) {
                if (t.equals(b.get(i))) {
                    return new Match(a0, i, 1);
                }
            }
            return Match.NULL;
        }

        if (m == 1) {
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

        //TODO  move all this into findMiddleSnake()...
        final boolean fromStart = a0 == xStart(middleSnakeEndpoint);
        final boolean toEnd = xEnd(middleSnakeEndpoint) >= a0 + n;

        if (fromStart && toEnd) {
            return diagonal;
        }

        Match before = fromStart ?
                null :
                lcs(a, a0, xStart(middleSnakeEndpoint) - a0,
                        b, b0, yStart(middleSnakeEndpoint) - b0);

        Match after = toEnd ?
                null :
                lcs(a, xEnd(middleSnakeEndpoint),
                        a0 + n - xEnd(middleSnakeEndpoint),
                        b, yEnd(middleSnakeEndpoint),
                        b0 + m - yEnd(middleSnakeEndpoint));

        if (fromStart) {
            return Match.chain(diagonal, after);

        } else if (toEnd) {
            return Match.chain(before, diagonal);
        }

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

                if (oddDelta && x > 0 && vb[maxk] <= x) {
                    if (delta < d) {
                        kStart = delta - (d - 1);
                        kEnd = delta + (d - 1);
                    } else {
                        kStart = delta + (d - 1);
                        kEnd = delta - (d - 1);
                    }
                    if(kStart <= k && k <= kEnd) {
                        xStart = isPrev ? next : prev + 1;
                        yStart = xStart - (k + (isPrev ? 1 : -1));

                        boundaries(endpoint, a0+xStart, b0+yStart, a0+x, b0+(x-k));
                        if (x > xMid) {
                            return new Match(a0+xMid, b0+(xMid-k), x-xMid);
                        } else {
                            return Match.NULL;
                        }
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;

                isVBounded = -max < kk && kk < max;

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

                if (!oddDelta && -d <= kk && kk <= d && x >= 0 && x <= vf[maxk]) {
                    xStart = isPrev ? prev : next - 1;
                    yStart = xStart - (kk + (isPrev ? -1 : 1));

                    final int a0XEnd = a0 + x;
                    final int b0YEnd = b0 + (x - kk);

                    boundaries(endpoint, a0XEnd, b0YEnd, a0+xStart, b0+yStart);
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
        @Deprecated // return null is faster!
        public static final Match NULL = new Match(-1, -1, 0);
        private final int x, y, steps;
        private Match next, last;
        private int lcs;

        private Match(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
            this.lcs = steps;
        }

        public int getLcs() {
            return lcs;
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
            Match head = NULL;
            Match current = null;
            for (Match s : segments) {
                if (s != null && s != NULL) {
                    if (head == NULL) {
                        current = head = s;
                    } else {
                        current.next = s;
                    }
                    while(true) {
                        if (current.last != null) {
                            current = current.last;
                        } else if (current.next != null) {
                            current = current.next;
                            head.lcs += current.lcs;
                        } else {
                            break;
                        }
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
                    "{xStart=" + x + ", yStart=" + y + ", steps=" + steps +
                    ", lcs=" + lcs + '}';
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

    private static Match boundaries(int[] endpoint,
            int xStart, int yStart, int xEnd, int yEnd) {
        endpoint[0] = xStart;
        endpoint[1] = yStart;
        endpoint[2] = xEnd;
        endpoint[3] = yEnd;
        return Match.NULL;
    }
}
