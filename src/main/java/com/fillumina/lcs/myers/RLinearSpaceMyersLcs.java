package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.VList;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.util.BidirectionalVector;
import java.util.ArrayList;
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
                    return new Snake(100, a0, b0, a0, i-1, a0+1, i);
                }
            }
            return Snake.NULL;
        }

        if (m == 1) {
            T t = b.get(1);
            for (int i=1; i<=n; i++) {
                if (t.equals(a.get(i))) {
                    return new Snake(100, a0, b0, i-1, b0, i, b0+1);
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
                            dMinusOne, k, a, n, b, m, delta, vr1);
                    if (xr <= xf) {
                        return findLastSnake(d, k, xf, a.zero(),b.zero(), vf1);
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = findFurthestReachingDPathReverse(d, kk, a, n, b, m, delta, vr1);
                if (!oddDelta && -delta <= kk && kk <= delta) {
                    xf = findFurthestReachingDPath(d, kk, a, n, b, m, vf1);
                    if (xr >= 0 && xr <= xf) {
                        return findLastSnakeReverse(d, kk, xr, a.zero(),b.zero(), vr1, n+1, m+1, delta);
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
            int delta, BidirectionalVector v) {
        int x, y;

        final int next = v.get(k + 1); // left
        final int prev = v.get(k - 1); // up
        if (k == d+delta || (k != -d+delta && prev < next)) {
            x = prev;   // up
        } else {
            x = next - 1;   // left
        }
        y = x - k;
        while (x > 0 && y > 0 && x <= n && y <= m &&
                a.get(x).equals(b.get(y))) {
            x--;
            y--;
        }
        v.set(k, x);
        return x;
    }

    private Snake findLastSnakeReverse(int d, int k, int xe, int x0, int y0,
            BidirectionalVector v, int n, int m, int delta) {
        int xEnd, yEnd, xMid, yMid, xStart = xe, yStart = xe - k;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        if (k == d+delta || (k != -d+delta && prev != 0 && prev < next)) {
            xEnd = prev;
            yEnd = prev - k + 1;
            xMid = xEnd;
        } else {
            xEnd = next;
            yEnd = next - k - 1;
            xMid = xEnd - 1;
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

}
