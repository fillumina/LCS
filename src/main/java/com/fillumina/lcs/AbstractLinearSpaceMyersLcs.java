package com.fillumina.lcs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the Linear Space Myers LCS algorithm. For maximum
 * flexibility its input is provided by extending the class.
 * It returns an ordered sequence of {@link LcsItem}s.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLinearSpaceMyersLcs {

    protected abstract int getFirstSequenceLength();
    protected abstract int getSecondSequenceLength();
    protected abstract boolean equals(int x, int y);

    public LcsItem calculateLcs() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        if (n == 0 || m == 0) {
            return null;
        }
        return lcsTail(0, n, 0, m, -1, null);
    }

    /**
     * At every step of the Myers Linear Space Algorithm it may happens
     * that there could be an equal head and tail. Because decreasing n and m
     * reduces calculations an easy check can avoid a lot of work.
     */
    private LcsItem lcsTail(final int a0, final int n,
            final int b0, final int m, final int equals, int[][] vv) {
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
        u += d;
        if (u < min) {
            //lcsMatch = lcsForwardMyers(a0 + d, n - u, b0 + d, m - u, new int[max * (max + 1)]);

//            System.out.println("equals=" + equals + " max=" + Math.max(n,m) + " min=" + min);
            int size;
            if (vv != null && equals < min &&
                    ((size = n + m + 1 - u) * (size + 1)) < vv[0].length) {
//                System.out.println("MYERS= " + vv[0].length );
                    lcsMatch = lcsForwardMyers(a0 + d, n - u, b0 + d, m - u, vv[0]);
            } else {
//                System.out.println("LCS");
                lcsMatch = lcsRec(a0 + d, n - u, b0 + d, m - u, vv);
            }
        }
        return LcsItem.chain(matchDown, lcsMatch, matchUp);
    }

    private LcsItem lcsRec(final int a0, final int n,
            final int b0, final int m, int[][] vv) {
        if (n == 1) {
            for (int i = b0; i < b0 + m; i++) {
                if (equals(a0, i)) {
                    return new LcsItem(a0, i, 1);
                }
            }
            return null;
        }
        if (m == 1) {
            for (int i = a0; i < a0 + n; i++) {
                if (equals(i, b0)) {
                    return new LcsItem(i, b0, 1);
                }
            }
            return null;
        }

        if (vv == null) {
            vv = new int[2][n+m+4];
        }

        LcsItem match = null;
        int xStart = -1;
        int yStart = -1;
        int xEnd = -1;
        int yEnd = -1;
        int fwEquals = 0;
        int bwEquals = 0;
        {
            // different scope to have less garbage on the stack when recursing
            final int max = (n + m + 1) >> 1;
            final int delta = n - m;
            final boolean evenDelta = (delta & 1) == 0;
            final int[] vf = vv[0];
            final int[] vb = vv[1];
            final int halfv = vf.length >> 1;

            vf[halfv + 1] = 0;
            vf[halfv + delta] = 0;
            vb[halfv - 1] = n;
            vb[halfv - delta] = n;

            boolean isPrev;
            int k;
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
                    while (xEnd < n && yEnd < m &&
                            equals(a0 + xEnd, b0 + yEnd)) {
                        xEnd++;
                        yEnd++;
                        fwEquals++;
                    }
                    vf[vIndex] = xEnd;
                    if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                            vb[vIndex - delta] <= xEnd) {
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

                kStart = delta - d;
                kEnd = delta + d;
                for (k = kStart; k <= kEnd; k += 2) {
                    vIndex = halfv + k - delta;

                    next = vb[vIndex + 1];
                    prev = vb[vIndex - 1];
                    isPrev = k == kEnd || (k != kStart && prev < next);
                    if (isPrev) {
                        xStart = prev; // up
                    } else {
                        xStart = next - 1; // left
                    }
                    yStart = xStart - k;
                    xMid = xStart;
                    while (xStart > 0 && yStart > 0 &&
                            equals(a0 + xStart - 1, b0 + yStart - 1)) {
                        xStart--;
                        yStart--;
                        bwEquals++;
                    }
                    vb[vIndex] = xStart;
                    if (evenDelta && -d <= k && k <= d && xStart >= 0 &&
                            xStart <= vf[vIndex + delta]) {
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

        // TODO put this stuff closer to the snake calculation
        LcsItem before = xStart <= 0 || yStart <= 0 || fwEquals == 0 ? null :
                lcsTail(a0, xStart, b0, yStart, fwEquals, vv);

        LcsItem after = xEnd >= n || n - xEnd == 0 || m - yEnd == 0 || bwEquals == 0
                ? null :
                lcsTail(a0 + xEnd, n - xEnd, b0 + yEnd, m - yEnd, bwEquals, vv);

        return LcsItem.chain(before, match, after);
    }


    private LcsItem lcsForwardMyers(int a0, int n, int b0, int m, int[] vv) {
        int max = n + m + 1;
        //int[] vv = new int[max * (max + 1)];

        int maxk, next, prev, x, y, d, k, vPrev, vNext = 1;
        for (d = 0; d < max; d++) {
            vPrev = vNext;
            vNext += (d << 1) + 1;
            for (k = -d; k <= d; k += 2) {
                maxk = vPrev + k;
                next = vv[maxk + 1]; // down
                prev = vv[maxk - 1]; // right
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
                vv[vNext + k] = x;

                if (x >= n && y >= m) {
                    LcsItem head=null;
                    int xStart, xMid;

                    for (; d >= 0 && x > 0; d--) {
                        maxk = vPrev + k;
                        next = vv[maxk + 1];
                        prev = vv[maxk - 1];
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
                            LcsItem  tmp = new LcsItem(
                                    a0 + xMid, b0 + xMid - k, x - xMid);
                            if (head == null) {
                                head = tmp;
                            } else {
                                head = tmp.chain(head);
                            }
                        }

                        x = xStart;
                        vPrev -= ((d-1) << 1) + 1;
                    }
                    return head;
                }
            }
        }
        return null;
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
