package com.fillumina.lcs;

import java.util.Iterator;

/**
 * This is an implementation of the Linear Space Myers LCS algorithm.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class LinearSpaceMyersLcs {

    protected abstract int getFirstSequenceLength();
    protected abstract int getSecondSequenceLength();
    protected abstract boolean equals(int x, int y);

    protected int getMaximumDistance() {
        return Integer.MAX_VALUE; //TODO check what happens exactly
    }

    public Match getLcsMatches() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        // TODO check about this magic number
        return lcsTail(0, n, 0, m, new int[2][3 *(n+m+1)]);
    }

    private Match lcsTail(final int a0, final int n,
            final int b0, final int m, int[][] vv) {
        final int min = n < m ? n : m;
        Match matchDown = null;
        Match matchUp = null;
        Match lcsMatch = null;
        int d;
        for (d = 0; d < min && equals(a0+d, b0+d); d++);
        if (d != 0) {
            matchDown = new Match(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u;
        int x0 = a0 + n - 1;
        int y0 = b0 + m - 1;
        for (u = 0; u < (min-d) && equals(x0 - u, y0 - u); u++);
        if (u != 0) {
            matchUp = new Match(a0 + n - u, b0 + m - u, u);
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
                    return new Match(a0, b0, 1);
                }
                return null;
            }
            for (int i = b0; i < b0 + m; i++) {
                if (equals(a0, i)) {
                    return new Match(a0, i, 1);
                }
            }
            return null;
        }
        if (m == 1) {
            for (int i = a0; i < a0 + n; i++) {
                if (equals(i, b0)) {
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
        {
            // different scope to have less garbage on the stack when recursing
            final int max = Math.min(getMaximumDistance(), (n + m + 1) / 2 + 1);
            final int delta = n - m;
            final boolean evenDelta = (delta & 1) == 0;
            final int[] vf = vv[0];
            final int[] vb = vv[1];
            final int halfv = vf.length >> 1;

            vf[halfv + 1] = 0;
            vb[halfv + delta - 1] = n;
            vb[halfv - delta - 1] = n;

            boolean isPrev;
            int k;
            int kDeltaStart;
            int kDeltaEnd;
            int prev;
            int next;
            int vIndex;
            int xMid;
            int kStart = delta - 1;
            int kEnd = delta + 1;

            FIND_MIDDLE_SNAKE:
            for (int d = 0; d <= max; d++) {
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
                    while (xEnd < n && yEnd < m && equals(a0 + xEnd, b0 + yEnd)) {
                        xEnd++;
                        yEnd++;
                    }
                    vf[vIndex] = xEnd;
                    if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                            vb[vIndex] <= xEnd) {
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
                    vIndex = halfv + k;
                    next = vb[vIndex + 1];
                    prev = vb[vIndex - 1];
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
                    vb[vIndex] = xStart;
                    if (evenDelta && -d <= k && k <= d && xStart >= 0 &&
                            xStart <= vf[vIndex]) {
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
        Match before = fromStart ? null :
                lcsTail(a0, xStart, b0, yStart, vv);
        Match after = toEnd || n - xEnd == 0 || m - yEnd == 0 ? null :
                lcsTail(a0 + xEnd, n - xEnd, b0 + yEnd, m - yEnd, vv);

        return Match.chain(before, match, after);
    }

    public static class Match implements Iterable<Match> {

        private static final long serialVersionUID = 1L;
        public static final Match NULL = new Match(-1, -1, 0);
        private final int x;
        private final int y;
        private final int steps;
        private Match next;
        private Match last;
        private int lcs;

        Match(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
            this.lcs = steps;
        }

        /**
         * This is NOT a general chain algorithm, it works because the way
         * matches are generated in the Myers LCS algorithms.
         */
        Match chain(final Match other) {
            Match current = this;
            if (last != null) {
                current = last;
            }
            current.next = other;
            lcs += other.lcs;
            if (other.last != null) {
                current = other.last;
            } else {
                current = other;
            }
            last = current;
            return this;
        }

        /** The value is valid only for the head match of the sequence. */
        public int getSequenceSize() {
            return lcs;
        }

        public int getFirstSequenceIndex() {
            return x;
        }

        public int getSecondSequenceIndex() {
            return y;
        }

        public int getSteps() {
            return steps;
        }

        abstract class IndexIterator implements Iterator<Integer> {
            private final Iterator<Match> i = Match.this.iterator();
            protected int step = 0;
            protected Match current;
            protected boolean hasNext;

            public IndexIterator() {
                increment();
            }

            protected final void increment() {
                while (current == null ||
                        current.steps == 0 ||
                        (step + 1) == current.steps) {
                    if (i.hasNext()) {
                        current = i.next();
                        step = -1;
                    } else {
                        hasNext = false;
                        return;
                    }
                }
                step++;
                hasNext = true;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }
        }

        public Iterable<Integer> lcsIndexesOfTheFirstSequence() {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    return new IndexIterator() {

                        @Override
                        public Integer next() {
                            final int result = current.x + step;
                            increment();
                            return result;
                        }
                    };
                }
            };
        }

        public Iterable<Integer> lcsIndexesOfTheSecondSequence() {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    return new IndexIterator() {

                        @Override
                        public Integer next() {
                            final int result = current.y + step;
                            increment();
                            return result;
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

        @Override
        public String toString() {
            if (this == NULL) {
                return "Match{NULL}";
            }
            return getClass().getSimpleName() +
                    "{xStart=" + x + ", yStart=" + y + ", steps=" + steps + '}';
        }

        static Match chain(Match before, Match middle,
                Match after) {
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

}
