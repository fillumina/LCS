package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        final VList<T> va = new VList<>(a);
        final VList<T> vb = new VList<>(b);
        Snake snakes = lcs(va, vb);
        return extractLcs(snakes, va);
    }

    Snake lcs(VList<T> a, VList<T> b) {
        int n = a.size();
        int m = b.size();
        if (n > 0 && m > 0) {
            Snake snake = findMiddleSnake(a, b);
            if (snake.d > 1) {
                Snake before = //FIXME the snake here referes to wrong indexes!
                    lcs(a.subList(1, snake.xStart), b.subList(1, snake.yStart));
                Snake after =
                    lcs(a.subList(snake.xEnd + 1, n), b.subList(snake.yEnd, m));
                return Snake.chain(before, snake, after);
            }

            int x0 = a.zero();
            int y0 = b.zero();

            if (m > n) {
                return new Snake(0, x0,y0, x0+n,x0, x0+n,y0);
            } else {
                return new Snake(0, x0,y0, x0,y0+m, x0,y0+m);
            }
        }
        return Snake.NULL;
    }

    Snake findMiddleSnake(VList<T> a, VList<T> b) {
        VList<T> ar = a.reverse();
        VList<T> br = b.reverse();

        int n = a.size();
        int m = b.size();

        int max = (int) Math.ceil(n + m / 2.0);

        BidirectionalVector vf1 = new BidirectionalVector(max);
        BidirectionalVector vr1 = new BidirectionalVector(max);
        BidirectionalVector vf2 = new BidirectionalVector(max);
        BidirectionalVector vr2 = new BidirectionalVector(max);

        int delta = n - m;
        boolean oddDelta = (delta & 1) == 1;
        int kk, xf, xr;
        for (int d = 0; d <= max; d++) {
            for (int k = -d; k <= d; k += 2) {

                xf = findFurthestReachingDPath(d, k, a, n, b, m, vf1);
                final int dMinusOne = d - 1;
                if (oddDelta &&
                        (delta - dMinusOne) <= k && k <= (delta + dMinusOne)) {
                    xr = n - findFurthestReachingDPath(
                            dMinusOne, k, ar, n, br, m, vr1);
                    if (xr <= xf) {
                        return findLastSnake(d, xf, a.zero(),b.zero(), k, vf1);
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = n - findFurthestReachingDPath(d, kk, ar, n, br, m, vr2);
                if (!oddDelta && -delta <= kk && kk <= delta) {
                    xf = findFurthestReachingDPath(d, kk, a, n, b, m, vf2);
                    if (xr <= xf) {
                        return findLastSnake(d, xr, ar.zero(),br.zero(), kk, vr2);
                    }
                }
            }
        }
        return Snake.NULL;
    }

    private int findFurthestReachingDPath(int d, int k,
            VList<T> a, int n, VList<T> b, int m,
            BidirectionalVector v) {
        int x, y;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        if (k == -d || (k != d && prev < next)) {
            x = next;
        } else {
            x = prev + 1;
        }
        y = x - k;
        while (x >= 0 && y >= 0 && x < n && y < m &&
                a.get(x + 1).equals(b.get(y + 1))) {
            x++;
            y++;
        }
        v.set(k, x);
        return x;
    }

    private Snake findLastSnake(int d, int x, int x0, int y0, int k,
            BidirectionalVector v) {
        int y = x - k;

        int xEnd = x;
        int yEnd = y;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        int xStart, yStart, xMid, yMid;
        if (k == -d || (k != d && prev < next)) {
            xStart = next;
            yStart = next - k - 1;
            xMid = xStart;
        } else {
            xStart = prev;
            yStart = prev - k + 1;
            xMid = xStart + 1;
        }

        yMid = xMid - k;
        return new Snake(d, x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    /**
     * @return the common subsequence elements.
     */
    private List<T> extractLcs(Snake snakes, VList<T> a) {
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            System.out.println(snake);
            for (int x = snake.xMid + 1; x <= snake.xEnd; x++) {
                list.add(a.get(x));
            }
        }
        return list;
    }

    /**
     * A snake is a sequence of equal
     * elements starting from mid to end and preceeded by a vertical or
     * horizontal edge going from start to mid.
     */
    static class Snake implements Iterable<Snake> {
        static final Snake NULL = new Snake(-1,-1,-1,-1,-1,-1,-1);
        public final int d, xStart, yStart, xMid, yMid, xEnd, yEnd;
        private Snake next;

        public Snake(int d, int xStart, int yStart, int xMid, int yMid, int xEnd,
                int yEnd) {
            this.d = d;
            this.xStart = xStart;
            this.yStart = yStart;
            this.xMid = xMid;
            this.yMid = yMid;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            System.out.println(toString());
        }

        /** @return the given {@link Snake}, or this if it is null. */
        static Snake chain(Snake... snakes) {
            Snake head = NULL;
            Snake current = null;
            for (Snake s : snakes) {
                if (s != null && s != NULL) {
                    if (head == NULL) {
                        current = head = s;
                    } else {
                        current.next = s;
                    }
                }
            }
            return head;
        }

        public boolean isRemoveLeft() {
            return xStart == xMid;
        }

        @Override
        public Iterator<Snake> iterator() {
            if (this == NULL) {
                return Collections.<Snake>emptyIterator();
            }
            return new Iterator<Snake>() {
                private Snake current = Snake.this;

                @Override
                public boolean hasNext() {
                    return current.next != null && current.next != NULL;
                }

                @Override
                public Snake next() {
                    return current = current.next;
                }
            };
        }

        @Override
        public String toString() {
            if (this == NULL) {
                return "Snake{NULL}";
            }
            return "Snake{" + "d=" + d +
                    ", xStart=" + xStart + ", yStart=" + yStart +
                    ", xMid=" + xMid + ", yMid=" + yMid + ", xEnd=" + xEnd +
                    ", yEnd=" + yEnd + '}';
        }
    }

    /**
     * A vector that allows for negative indexes.
     */
    static class BidirectionalVector {

        private final int[] array;
        private final int halfSize;

        public BidirectionalVector(int[] array) {
            this.halfSize = array.length >> 1;
            this.array = array;
        }

        /**
         * @param size specify the positive size (the total size will be
         * {@code size * 2 + 1}.
         */
        public BidirectionalVector(int size) {
            this.halfSize = size;
            this.array = new int[(halfSize << 1) + 1];
        }

        public int get(int x) {
            int index = halfSize + x;
            if (index < 0 || index >= array.length) {
                return 0;
            }
            return array[index];
        }

        public void set(int x, int value) {
            int index = halfSize + x;
            if (index < 0 || index >= array.length) {
                return;
            }
            array[index] = value;
        }
    }

    /** Reversable, sublistable vector with starting index 1. */
    static class VList<T> {

        private final List<T> list;
        private final int size;
        private final int start;
        private final int end;
        private final boolean reverse;

        public VList(List<T> list) {
            this(list, false);
        }

        private VList(List<T> list, boolean reverse) {
            this(list, 0, list == null ? 0 : list.size(), reverse);
        }

        private VList(List<T> list,
                int start, int end, boolean reverse) {
            this.list = list;
            this.start = start;
            this.end = end;
            this.size = end - start;
            this.reverse = reverse;
        }

        /** @return the first index of the wrapped list. */
        public int zero() {
            return start;
        }

        /** Note that the first index is 1 and not 0. */
        public T get(int index) {
            return list.get(calculateIndex(index));
        }

        private int calculateIndex(int index) {
            if (index <= 0 || index > size) {
                throw new IndexOutOfBoundsException(
                        "index (size=" + size + "): " + index);
            }
            int idx = start;
            if (reverse) {
                idx += (size - index);
            } else {
                idx += index - 1;
            }
            return idx;
        }

        public VList<T> subList(int fromIndex, int toIndex) {
            final int correctedFromIndex = calculateIndex(fromIndex);
            if (toIndex > size) {
                if (reverse) {
                    return new VList<>(list,
                            end - correctedFromIndex - 1,
                            end,
                            true);
                }
                return new VList<>(list,
                        correctedFromIndex,
                        end,
                        false);
            }
            final int correctedToIndex = calculateIndex(toIndex);
            if (reverse) {
                return new VList<>(list,
                        end - correctedFromIndex,
                        end - correctedToIndex,
                        true);
            }
            return new VList<>(list,
                    correctedFromIndex,
                    correctedToIndex,
                    false);
        }

        public VList<T> reverse() {
            return new VList<>(list, start, end, !reverse);
        }

        /** Note that the last index is {@code size}. */
        public int size() {
            return size;
        }

        @Override
        public String toString() {
            if (size() == 0) {
                return "[]";
            }
            StringBuilder buf = new StringBuilder().append('[');
            buf.append(get(1));
            for (int i=2; i<=size; i++) {
                buf.append(", ").append(get(i));
            }
            buf.append(']');
            return buf.toString();
        }
    }
}
