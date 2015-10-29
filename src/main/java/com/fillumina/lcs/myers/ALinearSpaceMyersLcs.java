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
        Match matches = lcsMain(a, b);
        return matches.extractLcsForFirstSequence(a);
    }

    public Match lcsMain(final List<T> a, final List<T> b) {
        final int n = a.size();
        final int m = b.size();
        return lcsTails(a, 0, n, b, 0, m);
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

        final int fullSize = n + m + 1;
        final int max = (fullSize >> 1) + 1; // ==> (fullSize / 2) + 1
        final int delta = n - m;
        final boolean oddDelta = (delta & 1) == 1; // delta is odd

        final int[][] vv = new int[2][fullSize+4]; // this can be externalized
        final int[] vf = vv[0];
        final int[] vb = vv[1];

        vb[max + delta - 1] = n;

        Match match = Match.NULL; //TODO put it to null
        int xStart=-1, yStart=-1, xEnd=-1, yEnd=-1, xMid=-1;
        { // put variables out of scope so to have less garbage on the stack
            boolean isPrev, isVBounded;
            int k, deltad, x, y, kStart, kEnd, prev, next, maxk;
            FOR:
            for (int d = 0; d < max; d++) {
                for (k = -d; k <= d; k += 2) {
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

                            xEnd = x;
                            yEnd = x - k;

                            //boundaries(endpoint, a0+xStart, b0+yStart, a0+xEnd, b0+yEnd);
                            if (x > xMid) {
                                match = new Match(a0+xMid, b0+(xMid-k), x-xMid);
                            } else {
                                match = Match.NULL;
                            }
                            break FOR;
                        }
                    }
                }

                deltad = delta + d;
                for (k = delta-d; k <= deltad; k += 2) {
                    isVBounded = -max < k && k < max;

                    maxk = max + k;
                    next = isVBounded ? vb[maxk + 1] : -1;
                    prev = isVBounded ? vb[maxk - 1] : -1;
                    isPrev = k == d + delta || (k != -d + delta && prev < next);
                    if (isPrev) {
                        x = prev;   // up
                    } else {
                        x = next - 1;   // left
                    }

                    xMid = x;
                    y = x - k;
                    while (x > 0 && y > 0 && x <= n && y <= m &&
                            a.get(a0+x-1).equals(b.get(b0+y-1))) {
                        x--;
                        y--;
                    }

                    if (x >= 0 && -max < k && k < max) {
                        vb[maxk] = x;
                    }

                    if (!oddDelta && -d <= k && k <= d && x >= 0 && x <= vf[maxk]) {
                        xStart = x;
                        yStart = x - k;

                        xEnd = isPrev ? prev : next - 1;
                        yEnd = xEnd - (k + (isPrev ? -1 : 1));

                        final int a0XEnd = a0 + x;
                        final int b0YEnd = b0 + (x - k);

                        //boundaries(endpoint, a0+xStart, b0+yStart, a0+xEnd, b0+yEnd);
                        if (xMid > x) {
                            match = new Match(a0XEnd, b0YEnd, xMid-x);
                        } else {
                            match = Match.NULL;
                        }
                        break FOR;
                    }
                }
            }
        }
        if (xStart == -1) {
            throw new AssertionError("BO");
        }

        final boolean fromStart = xStart == 0;
        final boolean toEnd = xEnd == n;

        if (fromStart && toEnd) {
            return match;
        }

        Match before = fromStart ? null :
                lcs(a, a0, xStart, b, b0, yStart);

        Match after = toEnd ? null :
                lcs(a, a0 + xEnd, n - xEnd, b, b0+ yEnd, m - yEnd);

        if (fromStart) {
            return Match.chain(match, after);

        } else if (toEnd) {
            return Match.chain(before, match);
        }

        return Match.chain(before, match, after);
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

        /**
         * @return the common subsequence elements.
         */
        public <T> List<T> extractLcsForFirstSequence(List<T> a) {
            List<T> list = new ArrayList<>();
            for (Match segment : this) {
                segment.addEquals(list, a);
            }
            return list;
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
}
