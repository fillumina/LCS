package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RLinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        final VList<T> va = new VList<>(a);
        final VList<T> vb = new VList<>(b);
        Snake snakes = lcs(va, vb);
        return extractLcs(snakes, va);
    }

    Snake lcs(VList<T> a, VList<T> b) {
        final int n = a.size();
        final int m = b.size();

        final int a0 = a.zero();
        final int b0 = b.zero();

        if (n == 0) {
            return new Snake(111, a0, b0, a0, m, a0, m);
        }

        if (m == 0) {
            return new Snake(111, a0, b0, n, b0, n, b0);
        }

        if (n == 1) {
            T t = a.get(1);
            for (int i=1; i<=m; i++) {
                if (t.equals(b.get(i))) {
                    return new Snake(100, a0, b0, a0, i-1, a0 + 1, i);
                }
            }
            return Snake.NULL;
        }

        if (m == 1) {
            T t = b.get(1);
            for (int i=1; i<=n; i++) {
                if (t.equals(a.get(i))) {
                    return new Snake(100, a0, b0, i - 1, b0, i, b0 + 1);
                }
            }
            return Snake.NULL;
        }

        Snake snake = findMiddleSnake(a, n, b, m);
        if (snake.d > 1) {
            Snake before =
                lcs(a.subList(1, snake.xStart + 1 - a0),
                        b.subList(1, snake.yStart + 1 - b0));
            Snake after =
                lcs(a.subList(snake.xEnd - a0, n + 1),
                        b.subList(snake.yEnd - b0, m + 1));
            return Snake.chain(before, snake, after);
        }

        return snake;
    }

    Snake findMiddleSnake(VList<T> a, int n, VList<T> b, int m) {
        final int max = (int) Math.ceil((n + m) / 2.0);

        final BidirectionalVector vf1 = new BidirectionalVector(max);
        final BidirectionalVector vr1 = new BidirectionalVector(max);

//        final BidirectionalVector vf2 = new BidirectionalVector(max);
//        final BidirectionalVector vr2 = new BidirectionalVector(max);

        final int delta = n - m;
        final boolean oddDelta = (delta & 1) == 1;

        for (int i=-max; i<=max; i++) {
            vr1.set(i, n);
        }
//        vr2.set(delta-1, n);
//        vr1.set(delta+1, n);
//        vr2.set(delta+1, n);

        int kk, xf, xr;
        for (int d = 0; d <= max; d++) {
            final int dMinusOne = d - 1;
            for (int k = -d; k <= d; k += 2) {

                xf = findFurthestReachingDPath(d, k, a, n, b, m, vf1);
                if (oddDelta &&
                        (delta - dMinusOne) <= k && k <= (delta + dMinusOne)) {
                    xr = findFurthestReachingDPathReverse(
                            dMinusOne, k, a, n, b, m, vr1);
                    if (xr <= xf) {
                        return findLastSnake(d, k, xf, a.zero(),b.zero(), vf1);
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = findFurthestReachingDPathReverse(d, kk, a, n, b, m, vr1);
                if (!oddDelta && -delta <= kk && kk <= delta) {
                    xf = findFurthestReachingDPath(d, kk, a, n, b, m, vf1);
                    if (xr >= 0 && xr <= xf) {
                        return findLastSnakeReverse(d, kk, xr, a.zero(),b.zero(), vr1, n+1, m+1);
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
        while (x >= 0 && y >= 0 && x + 1 < n && y + 1 < m &&
                a.get(x + 1).equals(b.get(y + 1))) {
            x++;
            y++;
        }
        v.set(k, x);
        return x;
    }

    private Snake findLastSnake(int d, int k, int x, int x0, int y0,
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

    private int findFurthestReachingDPathReverse(int d, int k,
            VList<T> a, int n, VList<T> b, int m,
            BidirectionalVector v) {
        int x, y;

        final int prev = v.get(k-1);
        final int next = v.get(k+1);
        if (k == d || (k != -d && prev < next)) {
            x = next - 1;
        } else {
            x = prev;
        }
        y = x - k;
        while (x > 1 && y > 1 && x < n && y < m && a.get(x - 1) == b.get(y - 1)) {
            x--;
            y--;
        }

        v.set(k, x);
        return x;
    }

    private Snake findLastSnakeReverse(int d, int k, int xe, int x0, int y0,
            BidirectionalVector v, int n, int m) {
        int x = xe, y;

        int xStart = x;
        int yStart = xe - k;

        final int prev = v.get(k-1);
        final int next = v.get(k+1);
        final int xMid, yMid;
        if (k == d || (k != -d && prev < next)) {
            x = next - 1;
        } else {
            x = prev;
        }
        y = x - k;
        xMid = x;
        yMid = y;

        final int xEnd = x;
        final int yEnd = y;

        v.set(k, x);
        return new Snake(d, x0+(n-xEnd), y0+(m-yEnd),
                x0+(n-xMid), y0+(n-yMid), x0+(n-xStart), y0+(m-yStart));
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
                    while(current.next != null) {
                        current = current.next;
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
            return new Iterator<Snake>() {
                private Snake current = Snake.this;

                @Override
                public boolean hasNext() {
                    return current != null && current != NULL;
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

        @Override
        public String toString() {
            return "" + halfSize + ":" + Arrays.toString(array);
        }
    }

    /** Reversable, sublistable vector with starting index 1. */
    static class VList<T> {
        @SuppressWarnings("unchecked")
        private static final VList<?> EMPTY = new VList<>(Collections.EMPTY_LIST);
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
            final int idx = calculateIndex(index);
            if (idx < start || idx >= end) {
                throw new IndexOutOfBoundsException(
                        "index (size=" + size + "): " + index);
            }
            if (idx < 0 || idx >= list.size()) {
                throw new IndexOutOfBoundsException(
                        "index (list size=" + size + "): " + index);
            }
            return list.get(idx);
        }

        private int calculateIndex(int index) {
            int idx = start;
            if (reverse) {
                idx += (size - index);
            } else {
                idx += index - 1;
            }
            return idx;
        }

        @SuppressWarnings("unchecked")
        public VList<T> subList(int fromIndex, int toIndex) {
            if (fromIndex < 1 || toIndex < 1) {
                throw new IndexOutOfBoundsException(
                        "indexes cannot be less than 1, to=" +
                                toIndex + ", from=" + fromIndex +
                                " " + toString());
            }
            final int correctedFromIndex = calculateIndex(fromIndex);
            int correctedToIndex = calculateIndex(toIndex);
            if (correctedFromIndex < 0 || correctedFromIndex >= list.size()) {
                throw new IndexOutOfBoundsException(
                        "from index out of boundaries " + correctedFromIndex +
                                " " + toString());
            }
            if (correctedToIndex < correctedFromIndex) {
                throw new IndexOutOfBoundsException(
                        "to index out of boundaries " + correctedToIndex +
                                " " + toString());
            }
            if (correctedFromIndex + size < correctedToIndex) {
                throw new IndexOutOfBoundsException("toIndex=" + toIndex +
                                " " + toString());
                //correctedToIndex = correctedFromIndex + size;
            }
            if (correctedFromIndex == correctedToIndex) {
                return (VList<T>) EMPTY;
            }
            if (reverse) {
                return new VList<>(list,
                        correctedToIndex + 1,
                        correctedFromIndex + 1,
                        true);
            }
            return new VList<>(list,
                    correctedFromIndex,
                    correctedToIndex,
                    false);
        }

        public VList<T> reverse() {
            if (size <= 1) {
                return this;
            }
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
