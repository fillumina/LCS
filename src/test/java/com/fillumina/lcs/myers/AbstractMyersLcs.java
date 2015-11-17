package com.fillumina.lcs.myers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractMyersLcs {
    protected abstract int getFirstSequenceLength();
    protected abstract int getSecondSequenceLength();
    protected abstract boolean equals(int x, int y);

    public LcsItem calculateLcs() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        LcsItem result = (n == 0 || m == 0) ? null :
            lcsTail(0, n, 0, m);
        if (result == null) {
            return LcsItem.NULL;
        }
        return result;
    }

    /**
     * At every step of the Myers Linear Space Algorithm it may happens
     * that there could be an equal head and tail. Because decreasing n and m
     * reduces calculations an easy check can avoid a lot of work.
     */
    private LcsItem lcsTail(final int a0, final int n,
            final int b0, final int m) {
        final int min = n < m ? n : m;
        LcsItem matchDown = null;
        LcsItem matchUp = null;
        LcsItem lcsMatch = null;
        int d = 0;
        if (equals(a0, b0)) {
            for (d = 1; d < min && equals(a0 + d, b0 + d); d++);
            matchDown = new LcsItem(a0, b0, d);
            if (d == min) {
                return matchDown;
            }
        }
        int u = 0;
        final int x0 = a0 + n - 1;
        final int y0 = b0 + m - 1;
        if (equals(x0, y0)) {
            final int maxu = min - d;
            for (u = 1; u < maxu && equals(x0 - u, y0 - u); u++);
            matchUp = new LcsItem(a0 + n - u, b0 + m - u, u);
        }
        if (u + d < min) {
            lcsMatch = lcsForwardMyers(
                    a0 + d, n - d - u, b0 + d, m - d - u);
        }
        return LcsItem.chain(matchDown, lcsMatch, matchUp);
    }

    private LcsItem lcsForwardMyers(int a0, int n, int b0, int m) {
        int max = n + m + 1;
        int size = (max << 1) + 1;
        int[][] vv = new int[max][size];
        int[] vNext, vPrev;

        int maxk, next, prev, x=-1, y, d, k=-1;
        FILL_THE_TABLE:
        for (d = 0; d < max; d++) {
            vPrev = vv[d == 0 ? 0 : d-1];
            vNext = vv[d];
            for (k = -d; k <= d; k += 2) {
                maxk = max + k;
                next = vPrev[maxk + 1]; // down
                prev = vPrev[maxk - 1]; // right
                if (k == -d || (k != d && prev < next)) {
                    x = next;
                } else {
                    x = prev + 1;
                }
                y = x - k;
                while (x < n && y < m && equals(a0 + x, b0 + y)) {
                    x++;
                    y++;
                }
                vNext[maxk] = x;
                if (x >= n && y >= m) {
                    break FILL_THE_TABLE;
                }
            }
        }

        LcsItem head=null;

        int xStart, xMid;
        for (; d >= 0 && x > 0; d--) {
            maxk = max + k;
            vNext = vv[d == 0 ? 0 : d-1];

            next = vNext[maxk + 1];
            prev = vNext[maxk - 1];
            if (k == -d || (k != d && prev < next)) {
                xStart = next;
                xMid = next;
                k++;
            } else {
                xStart = prev;
                xMid = prev + 1;
                k--;
            }

            if (x != xMid) {
                LcsItem  tmp = new LcsItem(xMid, xMid - k, x - xMid);
                if (head == null) {
                    head = tmp;
                } else {
                    head = tmp.chain(head);
                }
            }

            x = xStart;
        }
        return head;
    }


    public static class LcsItem
            implements Iterable<LcsItem>, Serializable {
        private static final long serialVersionUID = 1L;
        public static final LcsItem NULL = new LcsItem(-1, -1, 0);
        private final int x;
        private final int y;
        private final int steps;
        private LcsItem next;
        private LcsItem last;
        private int lcs;

        LcsItem(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
            this.lcs = steps;
        }

        /**
         * This is NOT a general chain algorithm, it works because the way
         * matches are generated in the Myers LCS algorithms.
         */
        LcsItem chain(final LcsItem other) {
            LcsItem current = this;
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
            private final Iterator<LcsItem> i = LcsItem.this.iterator();
            protected int step = 0;
            protected LcsItem current;
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
                            if (!hasNext) {
                                throw new NoSuchElementException();
                            }
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
                            if (!hasNext) {
                                throw new NoSuchElementException();
                            }
                            final int result = current.y + step;
                            increment();
                            return result;
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
                    "{xStart=" + x + ", yStart=" + y + ", steps=" + steps + '}';
        }

        static LcsItem chain(LcsItem before, LcsItem middle, LcsItem after) {
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
