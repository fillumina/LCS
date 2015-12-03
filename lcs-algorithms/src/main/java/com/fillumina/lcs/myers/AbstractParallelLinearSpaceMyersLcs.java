package com.fillumina.lcs.myers;

import java.util.concurrent.RecursiveTask;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractParallelLinearSpaceMyersLcs {
    private ThreadLocal<int[][]> cache;

    protected abstract int getFirstSequenceLength();
    protected abstract int getSecondSequenceLength();
    protected abstract boolean equals(int x, int y);

    public LcsItem calculateLcs() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        final int min = n < m ? n : m;
        cache = new ThreadLocal<int[][]>() {
            @Override
            protected int[][] initialValue() {
                return new int[2][2 * (n + m + 1)];
            }
        };

        LcsItem matchDown = null;
        LcsItem matchUp = null;
        LcsItem lcsMatch = null;
        int d;
        for (d = 0; d < min && equals(d, d); d++);
        if (d != 0) {
            matchDown = new LcsItem(0, 0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u;
        int x0 = n - 1;
        int y0 = m - 1;
        for (u = 0; u < (min-d) && equals(x0 - u, y0 - u); u++);
        if (u != 0) {
            matchUp = new LcsItem(n - u, m - u, u);
        }
        if (u + d != min) {
            lcsMatch = new RecursiveLcs(d, n - d - u, d, m - d - u).compute();
        }
        lcsMatch = LcsItem.chain(matchDown, lcsMatch, matchUp);
        return lcsMatch == null ? LcsItem.NULL : lcsMatch;
    }

    private class RecursiveLcs extends RecursiveTask<LcsItem> {
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
        protected LcsItem compute() {
            final int a0 = this.a0;
            final int n = this.n;
            final int b0 = this.b0;
            final int m = this.m;

            if (n == 0 || m == 0) {
                return null;
            }
            if (n == 1) {
                if (m == 1) {
                    if (AbstractParallelLinearSpaceMyersLcs.this.equals(a0, b0)) {
                        return new LcsItem(a0, b0, 1);
                    }
                    return null;
                }
                for (int i = b0; i < b0 + m; i++) {
                    if (AbstractParallelLinearSpaceMyersLcs.this.equals(a0, i)) {
                        return new LcsItem(a0, i, 1);
                    }
                }
                return null;
            }
            if (m == 1) {
                for (int i = a0; i < a0 + n; i++) {
                    if (AbstractParallelLinearSpaceMyersLcs.this.equals(i, b0)) {
                        return new LcsItem(i, b0, 1);
                    }
                }
                return null;
            }
            LcsItem match = null;
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
                                AbstractParallelLinearSpaceMyersLcs.this.equals(a0 + xEnd, b0 + yEnd)) {
                            xEnd++;
                            yEnd++;
                        }
                        vf[maxk] = xEnd;
                        if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                                vb[maxk] <= xEnd) {
                            if (xEnd > xMid) {
                                xStart = isPrev ? next : prev + 1;
                                yStart = xStart - (k + (isPrev ? 1 : -1));
                                match = new LcsItem(a0 + xMid, b0 + (xMid - k),
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
                                AbstractParallelLinearSpaceMyersLcs.this.equals(a0 + xStart - 1,
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
                                match = new LcsItem(a0 + xStart, b0 + yStart,
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

            LcsItem before = null;
            LcsItem after = null;
            RecursiveLcs beforeLcs = null;

            if (!fromStart)  {
                beforeLcs = new RecursiveLcs(a0, xStart, b0, yStart);
                if (Math.max(xStart - a0, yStart - b0) < 40) {
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

            return LcsItem.chain(before, match, after);
        }
    }

    public static class LcsItem implements Iterable<LcsItem>, Serializable {
        private static final long serialVersionUID = 1L;

        public static final LcsItem NULL = new LcsItem(-1, -1, 0);

        private final int x;
        private final int y;
        private final int steps;
        private LcsItem next;
        private LcsItem last;

        LcsItem(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }

        /**
         * This is NOT a general chain algorithm, it works because the way
         * matches are generated in the LCS algorithms.
         */
        LcsItem chain(final LcsItem other) {
            LcsItem current = this;
            if (last != null) {
                current = last;
            }
            current.next = other;
            accumulateLcs(other.getLcs());
            if (other.last != null) {
                current = other.last;
            } else {
                current = other;
            }
            last = current;
            return this;
        }

        protected void accumulateLcs(int otherLcs) {
            // do nothing
        }

        public int getLcs() {
            int lcs = 0;
            for (LcsItem m : this) {
                lcs += m.getSteps();
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
                        private Iterator<LcsItem> i = LcsItem.this.iterator();
                        private LcsItem current;
                        private int step = 0;

                        @Override
                        public boolean hasNext() {
                            while (current == null || current.steps == 0 ||
                                    (step + 1) == current.steps) {
                                if (i.hasNext()) {
                                    current = (LcsItem) i.next();
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
        public Iterator<LcsItem> iterator() {
            return new Iterator<LcsItem>() {
                private LcsItem current = LcsItem.this;

                @Override
                public boolean hasNext() {
                    return current != null && current != NULL;
                }

                @Override
                public LcsItem next() {
                    LcsItem tmp = current;
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
                    "{xStart=" + x + ", yStart=" + y +
                    ", steps=" + steps + '}';
        }

        public static LcsItem chain(LcsItem before, LcsItem middle, LcsItem after) {
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