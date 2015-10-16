package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs0<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        Snake snakes = recursiveLcs(a, b);
        return extractLcs(snakes, a);
    }

    Snake recursiveLcs(List<T> a, List<T> b) {
        final int n = a.size();
        final int m = b.size();

        if (n<=0 || m<=0) {
            return Snake.NULL;
        }

        final Snake ms = findMiddleSnake(a, n, b, m);

        if (ms.d <= 1) {
            return ms;
        } else {
            Snake before = recursiveLcs(
                    a.subList(0, ms.xStart), b.subList(0, ms.yStart));
            Snake after = recursiveLcs(
                    a.subList(ms.xEnd, n + 1), b.subList(ms.yEnd, m + 1));
            return Snake.chain(before, ms, after);
        }
    }

    Snake findMiddleSnake(List<T> a, int n, List<T> b, int m) {
        final int delta = n - m;
        final int dmax = (int) Math.ceil((n + m) / 2.0);
        boolean checkBwSnake = (delta & 1) == 0;

        BidirectionalVector vf = new BidirectionalVector(dmax);
        BidirectionalVector vb = new BidirectionalVector(dmax);
        final int deltaMinusOne = delta-1;
        vb.set(deltaMinusOne, n);

        Snake snake;

        for (int d = 0; d <= dmax; d++) {
            for (int k = -d; k <= d; k += 2) {
                snake = nextSnakeForward(a, n, b, m, k, -d, d, vf);

                if (!checkBwSnake && -d+deltaMinusOne <= k && k <= d+deltaMinusOne) {
                    if (vf.get(k) >= vb.get(k)) {
                        snake.d = 2 * d - 1;
                        return snake;
                    }
                }
            }

            for (int k = -d+delta; k <= d+delta; k += 2) {
                snake = nextSnakeBackward(a, n, b, m, k, d+delta, -d+delta, vb);

                if (checkBwSnake && -d <= k && k <= d) {
                    if (vf.get(k) >= vb.get(k)) {
                        snake.d = d * 2;
                        return snake;
                    }
                }
            }
        }
        return Snake.NULL;
    }

    private Snake nextSnakeForward(List<T> a, int n, List<T> b, int m,
            int k, int dStart, int dEnd,
            BidirectionalVector v) {
        int x, y;

        final int next = v.get(k+1);
        final int prev = v.get(k-1);

        final int xMid, yMid;
        if (k == dStart || (k != dEnd && prev < next)) {
            x = next;
            y = x - k;
            xMid = x;
            yMid = y;
        } else {
            x = prev + 1;
            y = x - k;
            xMid = x;
            yMid = y + 1;
        }

        final int xStart = x;
        final int yStart = y;

        while (x < n && y < m && a.get(x) == b.get(y)) {
            x++;
            y++;
        }

        final int xEnd = x;
        final int yEnd = y;

        v.set(k, x);
        return new Snake(xStart, yStart, xMid, yMid, xEnd, yEnd);
    }

    private Snake nextSnakeBackward(List<T> a, int n, List<T> b, int m,
            int k, int dStart, int dEnd,
            BidirectionalVector v) {
        int x, y;

        final int prev = v.get(k-1);
        final int next = v.get(k+1);
        final int xMid, yMid;
        if (k == dStart || (k != dEnd && prev < next)) {
            x = prev;
            y = x - k;
            xMid = x;
            yMid = y + 1;
        } else {
            x = next - 1;
            y = x - k;
            xMid = x;
            yMid = y - 1;
        }
        y = x - k;

        final int xEnd = x;
        final int yEnd = y;

        while (x > 0 && y > 0 && x < n && y < m && a.get(x) == b.get(y)) {
            x--;
            y--;
        }

        int xStart = x;
        int yStart = y;

        v.set(k, x);
        return new Snake(xStart, yStart, xMid, yMid, xEnd, yEnd);
    }

    /**
     * @return the common subsequence elements.
     */
    private List<T> extractLcs(Snake snakes, List<T> a) {
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            System.out.println("SOLUTION: " + snake);
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
        static final Snake NULL = new Snake(-1,-1,-1,-1,-1,-1);
        public final int xStart, yStart, xMid, yMid, xEnd, yEnd;
        private int d;
        private Snake next;

        public Snake(int xStart, int yStart, int xMid, int yMid, int xEnd,
                int yEnd) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xMid = xMid;
            this.yMid = yMid;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            System.out.println("CREATED: " + toString());
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
            this.array = new int[(halfSize << 1) + (size & 1)];
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
}
