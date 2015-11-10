package com.fillumina.lcs.myers;

import com.fillumina.lcs.Lcs;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The indexes are passed along the calls so to avoid using sublists.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedLinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(final List<T> a, final List<T> b) {
        final Match matches = lcsMatch(a, b);
        return matches.extractLcsForFirstSequence(a);
    }

    public Match lcsMatch(final List<T> a, final List<T> b) {
        final int n = a.size();
        final int m = b.size();
        @SuppressWarnings("unchecked")
        final Match match = lcsTails((T[])a.toArray(new Object[n]), n,
                (T[])b.toArray(new Object[m]), m);
        return match == null ? Match.NULL : match;
    }


    public Match lcsMatch(final T[] a, final T[] b) {
        final Match match = lcsTails(a, a.length, b, b.length);
        return match == null ? Match.NULL : match;
    }

    /** Recognizes equals head and tail so to speed up the calculations. */
    private Match lcsTails(final T[] a, final int n,
            final T[] b, final int m) {
        final int min = n < m ? n : m;
        Match matchDown = null, matchUp = null, lcsMatch = null;
        int d;
        for (d=0; d<min && Objects.equals(a[d],b[d]); d++);
        if (d != 0) {
            matchDown = new Match(0, 0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u, x0=n-1, y0=m-1;
        for (u=0; u<min && Objects.equals(a[x0-u],b[y0-u]); u++);
        if (u != 0) {
            matchUp = new Match(n-u, m-u, u);
        }

        if (u + d != min) {
            lcsMatch = lcsRec(a, d, n-d-u, b, d, m-d-u,
                    new int[2][2 * (n + m + 1)]);
        }

        return chain(matchDown, lcsMatch, matchUp);
    }

    protected Match lcsRec(final T[] a, final int a0, final int n,
            final T[] b, final int b0, final int m, int[][]vv) {

        if (n == 0 || m == 0) {
            return null;
        }

        if (n == 1) {
            if (m == 1) {
                if (Objects.equals(a[a0],b[b0])) {
                    return new Match(a0, b0, 1);
                }
                return null;
            }

            T t = a[a0];
            for (int i = b0; i < b0+m; i++) {
                if (Objects.equals(t,b[i])) {
                    return new Match(a0, i, 1);
                }
            }
            return null;
        }

        if (m == 1) {
            T t = b[b0];
            for (int i = a0; i < a0+n; i++) {
                if (Objects.equals(t,a[i])) {
                    return new Match(i, b0, 1);
                }
            }
            return null;
        }

        Match match = null;
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
                            match = new Match(a0+xMid, b0+(xMid-k), xEnd-xMid);
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
                            match = new Match(a0+xStart, b0+yStart, xMid-xStart);
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

        Match before = fromStart ? null :
                lcsRec(a, a0, xStart, b, b0, yStart, vv);

        Match after = toEnd || n-xEnd == 0 || m-yEnd == 0 ? null :
                lcsRec(a, a0+xEnd, n-xEnd, b, b0+yEnd, m-yEnd, vv);

        return chain(before, match, after);
    }

    private static Match chain(Match before, Match middle, Match after) {
        if (middle == null) {
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
                return middle;
            }
            return Match.chain(before, middle);
        }
        if (before == null) {
            return Match.chain(middle, after);
        }
        return Match.chain(before, Match.chain(middle, after));
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
            if (head.last != null) {
                current = head.last;
            }
            current.next = tail;
            head.lcs += tail.lcs;
            if (tail.last != null) {
                current = tail.last;
            } else {
                current = tail;
            }
            head.last = current;
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
