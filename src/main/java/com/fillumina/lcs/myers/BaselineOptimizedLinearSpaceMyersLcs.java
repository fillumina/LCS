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
public class BaselineOptimizedLinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(final List<T> a, final List<T> b) {
        Match matches = lcsMain(a, b);
        return matches.extractLcsForFirstSequence(a);
    }

    public Match lcsMain(final List<T> a, final List<T> b) {
        final int n = a.size();
        final int m = b.size();
        final Match match = lcsTails(a, n, b, m);
//        final Match match = lcsRec(a, 0, n, b, 0, m, new int[2][n + m + 5]);
        return match == null ? Match.NULL : match;
    }

    /** Recognizes equals head and tail so to speed up the calculations. */
    private Match lcsTails(final List<T> a, final int n,
            final List<T> b, final int m) {
        final int min = n < m ? n : m;
        Match matchDown = null, matchUp = null, lcsMatch = null;
        int d;
        for (d=0; d<min && a.get(d).equals(b.get(d)); d++);
        if (d != 0) {
            matchDown = new Match(0, 0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u, x0=n-1, y0=m-1;
        for (u=0; u<min && a.get(x0-u).equals(b.get(y0-u)); u++);
        if (u != 0) {
            matchUp = new Match(n-u, m-u, u);
        }

        if (u + d != min) {
            lcsMatch = lcsRec(a, d, n-d-u, b, d, m-d-u, new int[2][n+m+5]);
        }

        if (d > 0) {
            if (u > 0) {
                if (lcsMatch != null) {
                    return Match.chain(matchDown, Match.chain(lcsMatch, matchUp));
                }
                return Match.chain(matchDown, matchUp);
            }
            if (lcsMatch != null) {
                return Match.chain(matchDown, lcsMatch);
            }
            return matchDown;
        }
        if (u > 0) {
            if (lcsMatch != null) {
                return Match.chain(lcsMatch, matchUp);
            }
            return matchUp;
        }
        return lcsMatch;
    }

    protected Match lcsRec(final List<T> a, final int a0, final int n,
            final List<T> b, final int b0, final int m, int[][]vv) {

        if (n == 0 || m == 0) {
            return null;
        }

        if (n < 0 || m < 0) {
            throw new AssertionError("n=" + n + ", m=" + m);
        }

        if (a0 < 0 || b0 < 0) {
            throw new AssertionError("a0=" + a0 + ", b0=" + b0);
        }

        if (n == 1) {
            if (m == 1) {
                if (a.get(a0).equals(b.get(b0))) {
                    return new Match(a0, b0, 1);
                }
                return null;
            }

            T t = a.get(a0);
            for (int i = b0; i < b0+m; i++) {
                if (t.equals(b.get(i))) {
                    return new Match(a0, i, 1);
                }
            }
            return null;
        }

        if (m == 1) {
            T t = b.get(b0);
            for (int i = a0; i < a0+n; i++) {
                if (t.equals(a.get(i))) {
                    return new Match(i, b0, 1);
                }
            }
            return null;
        }

        boolean isEnded = false;
        Match match = null;
        int xStart=-1, yStart=-1, xEnd=-1, yEnd=-1;

        // find middle snake
        { // set variables out of scope so to have less garbage on the stack
            final int max = (n + m + 1) / 2 + 1; //(int)Math.ceil((n + m)/2.0);
            final int delta = n - m;
            final boolean evenDelta = (delta & 1) == 0;

            final int[] vf = vv[0];
            final int[] vb = vv[1];

            final int halfv = vf.length / 2;

            vf[halfv + 1] = 0;
            vb[halfv - 1] = n;
            vb[halfv - delta] = n;

            boolean isPrev;
            int k, deltad, kStart, kEnd, prev, next, maxk, xMid;
            FIND_MIDDLE_SNAKE:
            for (int d = 0; d <= max; d++) {
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
                    while (xEnd >= 0 && yEnd >= 0 && xEnd < n && yEnd < m &&
                            a.get(a0+xEnd).equals(b.get(b0+yEnd))) {
                        xEnd++;
                        yEnd++;
                    }
                    vf[maxk] = xEnd;

                    if (!evenDelta /*&& xEnd > 0 && maxk >= delta && vb[maxk - delta] <= xEnd */) {
                        if (d>1) {
                            kStart = delta - (d - 1);
                            kEnd = delta + (d - 1);
                        } else {
                            kStart = delta + (d - 1);
                            kEnd = delta - (d - 1);
                        }
                        assert kStart <= kEnd : "kStart=" + kStart +" > kEnd=" + kEnd;
                        if(kStart <= k && k <= kEnd &&
                                xEnd >=0 && vb[maxk - delta] <= xEnd) {

                            if (xEnd > xMid) {
                                xStart = isPrev ? next : prev + 1;
                                yStart = xStart - (k + (isPrev ? 1 : -1));
                                yStart = yEnd < yStart ? yEnd : yStart;
                                match = new Match(a0+xMid, b0+(xMid-k), xEnd-xMid);
                            } else {
                                xStart = isPrev ? next : prev;
                                yStart = xStart - (k + (isPrev ? 1 : -1));
                            }
                            isEnded = true;
                            break FIND_MIDDLE_SNAKE;
                        }
                    }
                }

                deltad = delta + d;
                for (k = delta-d; k <= deltad; k += 2) {

                    maxk = halfv + k - delta;
                    next = vb[maxk + 1];
                    prev = vb[maxk - 1];
                    isPrev = k == deltad || (k != delta-d && prev < next);
                    if (isPrev) {
                        xStart = prev;   // up
                    } else {
                        xStart = next - 1;   // left
                    }
                    yStart = xStart - k;

                    xMid = xStart;
                    while (xStart > 0 && yStart > 0 &&
                            xStart <= n && yStart <= m &&
                            a.get(a0+xStart-1).equals(b.get(b0+yStart-1))) {
                        xStart--;
                        yStart--;
                    }

                    if (xStart >= 0 /*&& -max < k && k < max*/) {
                        vb[maxk] = xStart;
                    }

                    if (evenDelta) {
                        if (d < 0) {
                            kStart = d;
                            kEnd = -d;
                        } else {
                            kStart = -d;
                            kEnd = d;
                        }
                        assert kStart <= kEnd : "kStart=" + kStart +" > kEnd=" + kEnd;
                        if(kStart <= k && k <= kEnd &&
                            xStart >= 0 && xStart <= vf[maxk + delta]) {

                            if (xMid > xStart) {
                                xEnd = isPrev ? prev : next - 1;
                                yEnd = xEnd - (k + (isPrev ? -1 : 1));
                                yEnd = yEnd < yStart ? yStart : yEnd;
                                match = new Match(a0+xStart, b0+yStart, xMid-xStart);
                            } else {
                                xEnd = isPrev ? prev : next;
                                yEnd = xEnd - (k + (isPrev ? -1 : 1));
                            }

                            isEnded = true;
                            break FIND_MIDDLE_SNAKE;
                        }
                    }
                }
            }
        }

        if (!isEnded) {
            return null;
        }

        final boolean fromStart = xStart <= 0;
        final boolean toEnd = xEnd >= n; // || xEnd == 0;

        if (fromStart && toEnd) {
            return match;
        }

        if (xStart == xEnd && yStart == yEnd) {
            System.out.println("middle snake not found!");
        }

        Match before = fromStart ? null :
                lcsRec(a, a0, xStart, b, b0, yStart, vv);

        Match after = toEnd || n-xEnd == 0 || m-yEnd == 0 ? null :
                lcsRec(a, a0+xEnd, n-xEnd, b, b0+yEnd, m-yEnd, vv);

        if (match == null) {
            if (after == null) {
                return before;
            }
            if (before == null) {
                return after;
            }
            return Match.chain(before, after);
        }
        if (after == null) {
            if (before == null) {
                return match;
            }
            return Match.chain(before, match);
        }
        if (before == null) {
            return Match.chain(match, after);
        }
        return Match.chain(before, Match.chain(match, after));
    }

    public static class Match implements Iterable<Match>, Serializable {
        private static final long serialVersionUID = 1L;
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

        /**
         * <b>NOTE</b> that this value is accurate only for the first element
         * of the iterable.
         */
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
            List<T> list = new ArrayList<>(getLcs());
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

        private static Match chain(final Match head, final Match tail) {
            assert head != null && tail != null;

            Match current = head;
            if (head.last == null) {
                while(current.next != null) {
                    current = current.next;
                    head.lcs += current.lcs;
                }
            } else {
                current = head.last;
            }
            current.next = tail;
            head.lcs += tail.lcs;
            if (tail.last != null) {
                current = tail.last;
            } else {
                current = tail;
                while(current.next != null) {
                    current = current.next;
                    head.lcs += current.lcs;
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

        /** @return a string representation of the entire iterable. */
        public static String toString(Match s) {
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
