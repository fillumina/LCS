package com.fillumina.lcs.myers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
// TODO add management of very long sequences
public abstract class ParallelLinearSpaceMyersLcs<T> {
    private ThreadLocal<int[][]> cache;

    abstract boolean equals(int x, int y);
    abstract int getLengthA();
    abstract int getLengthB();

    public Match getMatch() {
        final int n = getLengthA();
        final int m = getLengthB();
        final int min = n < m ? n : m;
        cache = new ThreadLocal<int[][]>() {
            @Override
            protected int[][] initialValue() {
                return new int[2][2 * (n + m + 1)];
            }
        };

        Match matchDown = null;
        Match matchUp = null;
        Match lcsMatch = null;
        int d;
        for (d = 0; d < min && equals(d, d); d++) {
            ;
        }
        if (d != 0) {
            matchDown = new Match(0, 0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u;
        int x0 = n - 1;
        int y0 = m - 1;
        for (u = 0; u < min && equals(x0 - u, y0 - u); u++) {
            ;
        }
        if (u != 0) {
            matchUp = new Match(n - u, m - u, u);
        }
        if (u + d != min) {
            lcsMatch = new RecursiveLcs(d, n - d - u, d, m - d - u).compute();
        }
        return chain(matchDown, lcsMatch, matchUp);
    }

    private class RecursiveLcs extends RecursiveTask<Match> {
        private static final long serialVersionUID = 1L;
        private final int a0, n, b0, m;

        public RecursiveLcs(int a0, int n, int b0, int m) {
            super();
            this.a0 = a0;
            this.n = n;
            this.b0 = b0;
            this.m = m;
        }

        @Override
        protected Match compute() {
            final int a0 = this.a0;
            final int n = this.n;
            final int b0 = this.b0;
            final int m = this.m;
            
            if (n == 0 || m == 0) {
                return null;
            }
            if (n == 1) {
                if (m == 1) {
                    if (ParallelLinearSpaceMyersLcs.this.equals(a0, b0)) {
                        return new Match(a0, b0, 1);
                    }
                    return null;
                }
                for (int i = b0; i < b0 + m; i++) {
                    if (ParallelLinearSpaceMyersLcs.this.equals(a0, i)) {
                        return new Match(a0, i, 1);
                    }
                }
                return null;
            }
            if (m == 1) {
                for (int i = a0; i < a0 + n; i++) {
                    if (ParallelLinearSpaceMyersLcs.this.equals(i, b0)) {
                        return new Match(i, b0, 1);
                    }
                }
                return null;
            }
            Match match = null;
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
                final int vv[][] = cache.get();
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
                        while (xEnd < n && yEnd < m &&
                                ParallelLinearSpaceMyersLcs.this.equals(a0 + xEnd, b0 + yEnd)) {
                            xEnd++;
                            yEnd++;
                        }
                        vf[maxk] = xEnd;
                        if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                                vb[maxk] <= xEnd) {
                            if (xEnd > xMid) {
                                xStart = isPrev ? next : prev + 1;
                                yStart = xStart - (k + (isPrev ? 1 : -1));
                                match = new Match(a0 + xMid, b0 + (xMid - k),
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
                        while (xStart > 0 && yStart > 0 &&
                                ParallelLinearSpaceMyersLcs.this.equals(a0 + xStart - 1,
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
                                match = new Match(a0 + xStart, b0 + yStart,
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

            Match before = null, after = null;
            RecursiveLcs beforeLcs = null;

            if (!fromStart)  {
                beforeLcs = new RecursiveLcs(a0, xStart, b0, yStart);
                if (Math.max(xStart - a0, yStart - b0) < 8) {
                    before = beforeLcs.compute();
                    beforeLcs = null;
                } else {
                    beforeLcs.fork();
                }
            }

            if (!(toEnd || n - xEnd == 0 || m - yEnd == 0)) {
                after = new RecursiveLcs(a0 + xEnd, n - xEnd, b0 + yEnd, m - yEnd)
                        .compute();
            }

            if (beforeLcs != null) {
                before = beforeLcs.join();
            }

            return chain(before, match, after);
        }
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
        private final int x;
        private final int y;
        private final int steps;
        private Match next;
        private Match last;

        private Match(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }

        private static Match chain(final Match head, final Match tail) {
            assert head != null && tail != null;
            Match current = head;
            if (head.last != null) {
                current = head.last;
            }
            current.next = tail;
            // parallel LCS cannot calculate this reliably
            //head.lcs += tail.lcs;
            if (tail.last != null) {
                current = tail.last;
            } else {
                current = tail;
            }
            head.last = current;
            return head;
        }

        /**
         * <b>NOTE</b> that this value is accurate only for the first
         * element of the iterable.
         */
        public int getLcs() {
            int lcs = 0;
            for (Match m : this) {
                lcs += m.steps;
            }
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

        public Iterable<Integer> lcsIndexes() {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    return new Iterator<Integer>() {
                        Iterator<Match> i = Match.this.iterator();
                        Match current;
                        int step = 0;

                        @Override
                        public boolean hasNext() {
                            while (current == null || current.steps == 0 ||
                                    (step + 1) == current.steps) {
                                if (i.hasNext()) {
                                    current = i.next();
                                    step = -1;
                                } else {
                                    return false;
                                }
                            }
                            step++;
                            return true;
                        }

                        @Override
                        public Integer next() {
                            return current.x + step;
                        }
                    };
                }
            };
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

        /**
         * @return a string representation of the entire iterable.
         */
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
                    "{xStart=" + x + ", yStart=" + y +
                    ", steps=" + steps + '}';
        }
    }

}
