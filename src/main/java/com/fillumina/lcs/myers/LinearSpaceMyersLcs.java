package com.fillumina.lcs.myers;

import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.util.BidirectionalVector;
import com.fillumina.lcs.util.VList;
import java.util.ArrayList;
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
        final int n = a.size();
        final int m = b.size();

        if (n == 1) {
            T t = a.get(1);
            for (int i=1; i<=m; i++) {
                if (t.equals(b.get(i))) {
                    final int a0 = a.zero();
                    final int b0 = b.zero();
                    return new Snake(2, a0, b0, a0, i-1, a0 + 1, i);
                }
            }
            return Snake.NULL;
        }

        if (m == 1) {
            T t = b.get(1);
            for (int i=1; i<=n; i++) {
                if (t.equals(a.get(i))) {
                    final int a0 = a.zero();
                    final int b0 = b.zero();
                    return new Snake(2, a0, b0, i - 1, b0, i, b0 + 1);
                }
            }
            return Snake.NULL;
        }

        final int a0 = a.zero();
        final int b0 = b.zero();

        if (n > 0 && m > 0) {
            Snake snake = findMiddleSnake(a, b);
            if (snake.d > 1) {
                Snake before = //FIXME the snake here referes to wrong indexes!
                    lcs(a.subList(1, snake.xStart + 1 - a0),
                            b.subList(1, snake.yStart + 1 - b0));
                Snake after =
                    lcs(a.subList(snake.xEnd - a0, n + 1),
                            b.subList(snake.yEnd - b0, m + 1));
                return Snake.chain(before, snake, after);
            }

            return snake;
//            int x0 = a.zero();
//            int y0 = b.zero();
//
//            if (m > n) {
//                return new Snake(0, x0,y0, x0+n,x0, x0+n,y0);
//            } else {
//                return new Snake(0, x0,y0, x0,y0+m, x0,y0+m);
//            }
        }
        return Snake.NULL;
    }

    Snake findMiddleSnake(VList<T> a, VList<T> b) {
        int n = a.size();
        int m = b.size();

        VList<T> ar = a.reverse();
        VList<T> br = b.reverse();

        int max = (int) Math.ceil((n + m) / 2.0);

        BidirectionalVector vf1 = new BidirectionalVector(max);
        BidirectionalVector vr1 = new BidirectionalVector(max);
        BidirectionalVector vf2 = new BidirectionalVector(max);
        BidirectionalVector vr2 = new BidirectionalVector(max);

        int delta = n - m;
        boolean oddDelta = (delta & 1) == 1;
        int kk, xf, xr;
        for (int d = 0; d <= max; d++) {
            final int dMinusOne = d - 1;
            for (int k = -d; k <= d; k += 2) {

                xf = findFurthestReachingDPath(d, k, a, n, b, m, vf1);
                if (oddDelta &&
                        (delta - dMinusOne) <= k && k <= (delta + dMinusOne)) {
                    xr = n - findFurthestReachingDPath(
                            dMinusOne, k, ar, n, br, m, vr1);
                    if (xr <= xf) {
                        return findLastSnake(d, k, xf, a.zero(),b.zero(), vf1, false, 0,0);
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = n - findFurthestReachingDPath(d, kk, ar, n, br, m, vr2);
                if (!oddDelta && -delta <= kk && kk <= delta) {
                    xf = findFurthestReachingDPath(d, kk, a, n, b, m, vf2);
                    if (xr <= xf) {
                        return findLastSnake(d, kk, n - xr, ar.zero(),br.zero(), vr2, true, n+1, m+1);
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

    private Snake findLastSnake(int d, int k, int x, int x0, int y0,
            BidirectionalVector v, boolean reverse, int n, int m) {
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
        if (reverse) {
            return new Snake(d, x0+(n-xEnd), y0+(m-yEnd),
                x0+(n-xMid), y0+(m-yMid), x0+(n-xStart), y0+(m-yStart));
        }
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
