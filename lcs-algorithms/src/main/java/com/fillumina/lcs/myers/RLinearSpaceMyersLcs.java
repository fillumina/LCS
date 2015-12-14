package com.fillumina.lcs.myers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.fillumina.lcs.Lcs;

/**
 * Myers algorithm that uses forward and backward snakes. It is not designed
 * to be performant but to be easy to understand.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RLinearSpaceMyersLcs implements Lcs {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        return new LcsSolver<>(a, b).calculateLcs();
    }

    static class LcsSolver<T> {
        private final List<? extends T> a, b;

        public LcsSolver(List<? extends T> a, List<? extends T> b) {
            this.a = a;
            this.b = b;
        }

        List<? extends T> calculateLcs() {
            Snake snakes = new Section(0,0, a.size(),b.size()).lcs();
            return extractLcs(snakes);
        }

        /** @return the common subsequence elements. */
        private List<? extends T> extractLcs(Snake snakes) {
            List<T> list = new ArrayList<>();
            List<T> tmp = new ArrayList<>();
            int x=0;
            boolean error=false;
            for (Snake snake : snakes) {
                if (snake.xStart != x) {
                    error = true;
                }
                tmp.clear();
                for (int index : snake.getDiagonal()) {
                    list.add(a.get(index));
                }
                x = snake.xEnd;
            }
            if (error) {
                throw new AssertionError("uncomplete chain");
            }
            return list;
        }

        class Section extends Rectangle {

            Section(int xStart, int yStart, int xEnd, int yEnd) {
                super(xStart, yStart, xEnd, yEnd);
            }

            <T> Snake lcs() {
                if (isImproper()) {
                    return createNullSnake();
                }

                Snake snake = findMiddleSnake();
                if (isMaximum(snake)) {
                    return snake;
                }

                Snake before = getSectionBefore(snake).lcs();
                Snake after = getSectionAfter(snake).lcs();

                return Snake.chain(before, snake, after);
            }

            <T> Snake findMiddleSnake() {
                // (int)Math.ceil((m + n)/2.0);
                final int max = ((n + m + 1) >> 1) + 1;
                final int delta = n - m;
                // delta % 2 == 0
                final boolean evenDelta = (delta & 1) == 0;

                final BidirectionalVector vf = new BidirectionalVector(max);
                final BidirectionalVector vb = new BidirectionalVector(max, delta);

                vf.set(1, 0);
                vb.set(0, n);
                vb.set(delta-1, n);

                int kk, xf, xr, start, end;
                for (int d = 0; d <= max; d++) {
                    start = delta - (d - 1);
                    end = delta + (d - 1);
                    for (int k = -d; k <= d; k += 2) {
                        xf = findFurthestReachingDPath(d, k, vf);
                        if (!evenDelta && isIn(k, start, end) && vb.get(k) <= xf) {
                            return findLastSnake(d, k, vf, xf);
                        }
                    }

                    for (int k = -d; k <= d; k += 2) {
                        kk = k + delta;
                        xr = findFurthestReachingDPathReverse(d, kk, delta, vb);
                        if (evenDelta && isIn(kk, -d, d) && xr >= 0 && xr <= vf.get(kk)) {
                            return findLastSnakeReverse(d, kk, delta, vb, xr);
                        }
                    }
                }

                return createNullSnake();
            }

            private int findFurthestReachingDPath(int d, int k,
                    BidirectionalVector vf) {
                int x, y;

                int next = vf.get(k + 1);
                int prev = vf.get(k - 1);
                if (k == -d || (k != d && prev < next)) {
                    x = next;
                } else {
                    x = prev + 1;
                }
                y = x - k;
                while (x >= 0 && y >= 0 && x < n && y < m && sameItem(x, y)) {
                    x++;
                    y++;
                }
                vf.set(k, x);
                return x;
            }

            private Snake findLastSnake(int d, int k,
                    BidirectionalVector vf, int x) {
                int y = x - k;

                int xEnd = x;
                int yEnd = y;

                int next = vf.get(k + 1);
                int prev = vf.get(k - 1);
                int xStart, yStart, xMid, yMid;
                if (k == -d) {
                    xStart = next;
                    yStart = xStart - k;
                    xMid = xStart;
                } else if (k != d && prev < next) {
                    xStart = next;
                    yStart = xStart - k - 1;
                    xMid = xStart;
                } else {
                    xStart = prev;
                    yStart = xStart - k + 1;
                    xMid = xStart + 1;
                }

                yMid = xMid - k;

                if (xMid == xEnd && yMid == yEnd) {
                    xMid = xStart;
                    yMid = yStart;
                    while (xStart >0 && yStart >0 &&
                            sameItem(xStart-1, yStart-1)) {
                        xStart--;
                        yStart--;
                    }
                    return createReverseSnake(xStart, yStart,
                        xMid, yMid, xEnd, yEnd);
                }

                return createForwardSnake(xStart, yStart,
                        xMid, yMid, xEnd, yEnd);
            }

            private <T> int findFurthestReachingDPathReverse(
                    int d, int k, int delta, BidirectionalVector vb) {
                int x, y;

                final int next = vb.get(k + 1); // left
                final int prev = vb.get(k - 1); // up
                if (k == d+delta || (k != -d+delta && prev < next)) {
                    x = prev;   // up
                } else {
                    x = next - 1;   // left
                }
                y = x - k;
                while (x > 0 && y > 0 && x <= n && y <= m &&
                        sameItem(x-1, y-1)) {
                    x--;
                    y--;
                }
                if (x >= 0) {
                    vb.set(k, x);
                }
                return x;
            }

            private <T> Snake findLastSnakeReverse(int d, int k, int delta,
                    BidirectionalVector vb, int xe) {
                final int xStart = xe;
                final int yStart = xe - k;
                int xEnd, yEnd, xMid, yMid;

                final int next = vb.get(k + 1);
                final int prev = vb.get(k - 1);
                if (k == d+delta) {
                    xEnd = prev;
                    yEnd = prev - k;
                    xMid = xEnd;
                } else if (k != -d+delta && prev != 0 && prev < next) {
                    xEnd = prev;
                    yEnd = prev - k + 1;
                    xMid = xEnd;
                } else {
                    xEnd = next;
                    yEnd = next - k - 1;
                    xMid = xEnd - 1;
                }
                yMid = xMid - k;

                if (xMid == xStart && yMid == yStart) {
                    xMid = xEnd;
                    yMid = yEnd;
                    while (xEnd < n && yEnd < m && sameItem(xEnd, yEnd)) {
                        xEnd++;
                        yEnd++;
                    }
                    return createForwardSnake(xStart, yStart,
                        xMid, yMid, xEnd, yEnd);
                }

                return createReverseSnake(xStart, yStart,
                        xMid, yMid, xEnd, yEnd);
            }

            /** @return the innner rectangle getSectionBefore the given one. */
            Section getSectionBefore(Rectangle o) {
                return new Section(xStart, yStart, o.xStart, o.yStart);
            }

            /** @return the innner rectangle getSectionAfter the given one. */
            Section getSectionAfter(Rectangle o) {
                return new Section(o.xEnd, o.yEnd, xEnd, yEnd);
            }

            Snake createForwardSnake(int xStart, int yStart,
                    int xMid, int yMid,
                    int xEnd, int yEnd) {
                int x0 = this.xStart, y0 = this.yStart;
                return new ForwardSnake(x0+xStart, y0+yStart,
                    x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
            }

            Snake createReverseSnake(int xStart, int yStart,
                    int xMid, int yMid,
                    int xEnd, int yEnd) {
                int x0 = this.xStart, y0 = this.yStart;
                return new ReverseSnake(x0+xStart, y0+yStart,
                    x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);

            }

            Snake createNullSnake() {
                return new Snake(xStart, yStart, xStart, yStart, xEnd, yEnd);
            }

            boolean sameItem(int aIndex, int bIndex) {
                T aItem = a.get(xStart + aIndex);
                T bItem = b.get(yStart + bIndex);
                return aItem == bItem || (aItem != null && aItem.equals(bItem));
            }
        }
    }

    /**
     * A 2D area described by the top,left - bottom,right coordinates
     * considering the origin on the top-left.
     */
    static class Rectangle {
        final int xStart, yStart;
        final int xEnd, yEnd;
        final int n, m;

        public Rectangle(List<?> a, List<?> b) {
            this(0, 0, a.size(), b.size());
        }

        public Rectangle(int xStart, int yStart, int xEnd, int yEnd) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            this.n = xEnd - xStart;
            this.m = yEnd - yStart;
        }

        /** Has zero width or height. */
        boolean isImproper() {
            return xStart == xEnd || yStart == yEnd;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + this.xStart;
            hash = 79 * hash + this.yStart;
            hash = 79 * hash + this.xEnd;
            hash = 79 * hash + this.yEnd;
            return hash;
        }

        public boolean isMaximum(Rectangle other) {
            return xStart == other.xStart &&
                    yStart == other.yStart &&
                    xEnd == other.xEnd &&
                    yEnd == other.yEnd;
        }
    }

    static class Snake extends Rectangle implements Iterable<Snake> {
        public final int xMid, yMid;
        private Snake next;

        private Snake(int xStart, int yStart,
                int xMid, int yMid,
                int xEnd, int yEnd) {
            super(xStart, yStart, xEnd, yEnd);
            this.xMid = xMid;
            this.yMid = yMid;
        }

        /** @return the given {@link Snake}, or this if it is null. */
        static Snake chain(Snake... snakes) {
            Snake head = null;
            Snake current = null;
            for (Snake s : snakes) {
                if (s != null) {
                    if (head == null) {
                        current = head = s;
                    } else {
                        current.next = s;
                    }
                    while(current.next != null) {
                        current = current.next;
                    }
                }
            }
            return head;
        }

        public Interval getDiagonal() {
            return Interval.EMPTY;
        }

        @Override
        public Iterator<Snake> iterator() {
            return new Iterator<Snake>() {
                private Snake current = Snake.this;

                @Override
                public boolean hasNext() {
                    return current != null;
                }

                @Override
                public Snake next() {
                    Snake tmp = current;
                    current = current.next;
                    return tmp;
                }
            };
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() +
                    "{xStart=" + xStart + ", yStart=" + yStart +
                    ", xMid=" + xMid + ", yMid=" + yMid + ", xEnd=" + xEnd +
                    ", yEnd=" + yEnd + '}';
        }
    }

    private static class ForwardSnake extends Snake {

        public ForwardSnake(int xStart, int yStart, int xMid,
                int yMid, int xEnd, int yEnd) {
            super(xStart, yStart, xMid, yMid, xEnd, yEnd);
        }

        @Override
        public Interval getDiagonal() {
            return new Interval(xMid, xEnd);
        }
    }

    private static class ReverseSnake extends Snake {

        public ReverseSnake(int xStart, int yStart, int xMid,
                int yMid, int xEnd, int yEnd) {
            super(xStart, yStart, xMid, yMid, xEnd, yEnd);
        }

        @Override
        public Interval getDiagonal() {
            return new Interval(xStart, xMid);
        }

    }

    static class Interval implements Iterable<Integer> {
        static final Interval EMPTY = new Interval(0,-1);
        private final int start, end;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean isEmty() {
            return start >= end;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                private int current = start;

                @Override
                public boolean hasNext() {
                    return current < end;
                }

                @Override
                public Integer next() {
                    int tmp = current;
                    current++;
                    return tmp;
                }
            };
        }
    }

    static boolean isIn(int value, int startInterval, int endInterval) {
        if (startInterval < endInterval) {
            if (value < startInterval) {
                return false;
            }
            if (value > endInterval) {
                return false;
            }
        }
        else {
            if (value > startInterval) {
                return false;
            }
            if (value < endInterval) {
                return false;
            }
        }
        return true;
    }
}
