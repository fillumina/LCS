package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.VList;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.util.BidirectionalVector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Myers algorithm that uses forward and backward snake.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RLinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        final VList<T> va = new VList<>(a);
        final VList<T> vb = new VList<>(b);
        Snake snakes = lcs(va, vb);
        return extractLcs(snakes, va, vb);
    }

    Snake lcs(VList<T> a, VList<T> b) {
        final int n = a.size();
        final int m = b.size();

        final int a0 = a.zero();
        final int b0 = b.zero();

        if (n == 0) {
            if (m != 0) {
                return new Snake(false, -111, a0, b0, a0, b0+m, a0, b0+m);
            }
            return Snake.NULL;
        }

        if (m == 0) {
            if (n != 0) {
                return new Snake(false, -111, a0, b0, a0+n, b0, a0+n, b0);
            }
            return Snake.NULL;
        }

        if (n == 1) {
            if (m == 1) {
                if (a.get(1).equals(b.get(1))) {
                    return new Snake(false, -333, a0,b0, a0,b0, a0+1,b0+1);
                }
                return new Snake(false, -333, a0,b0, a0+1,b0+1, a0+1,b0+1);
            }
            T t = a.get(1);
            for (int i=1; i<=m; i++) {
                if (t.equals(b.get(i))) {
                    if (i == 1) {
                        return new Snake(true, -100, a0, b0, a0+1, b0+i, a0+1, b0+m);
                    } else if (i == m) {
                        return new Snake(false, -100, a0, b0, a0, b0+m-1, a0+1, b0+m);
                    } else {
                        return Snake.chain(
                            new Snake(false, -100, a0, b0, a0, b0+i-1, a0+1, b0+i),
                            new Snake(false, -100, a0+1, b0+i, a0+1, b0+i, a0+1, b0+m)
                        );
                    }
                }
            }
            return new Snake(false, -222, a0,b0, a0+1,b0, a0+1,b0+m);
        }

        if (m == 1) {
            T t = b.get(1);
            for (int i=1; i<=n; i++) {
                if (t.equals(a.get(i))) {
                    if (i == 1) {
                        return new Snake(true, -100, a0, b0, a0+i, b0+1, a0+n, b0+1);
                    } else if (i == n) {
                        return new Snake(false, -100, a0, b0, a0+n-1, b0+1, a0+n, b0+1);
                    }
                    return Snake.chain(
                            new Snake(false, -100, a0, b0, a0+i-1, b0+1, a0+i, b0+1),
                            new Snake(false, -100, a0+i, b0+1, a0+n, b0+m, a0+n, b0+m));
                }
            }
            return new Snake(false, -222, a0,b0, a0+n,b0, a0+n,b0+1);
        }

        Snake snake = findMiddleSnake(a, n, b, m);
        if (snake != Snake.NULL) {
            Snake before =
                lcs(a.subList(1, snake.xStart + 1 - a0),
                        b.subList(1, snake.yStart + 1 - b0));
            Snake after =
                lcs(a.subList(snake.xEnd + 1 - a0, n + 1),
                        b.subList(snake.yEnd + 1 - b0, m + 1));
            return Snake.chain(before, snake, after);
        }
        return snake;
    }

    Snake findMiddleSnake(VList<T> a, int n, VList<T> b, int m) {
        assert (m + n) / 2 == (int)Math.ceil((n + m ) / 2) : "m=" + m + ", n=" + n;
        final int max = (m + n) / 2;
        final int delta = n - m;
        final boolean oddDelta = (delta & 1) == 1;

        final BidirectionalVector vf = new BidirectionalVector(max + 1); //ok
        final BidirectionalVector vb = new BidirectionalVector(max + 1, delta);

        vb.set(delta-1, n);

        int kk, xf, xr, start, end;
        for (int d = 0; d <= max; d++) {
            start = delta - (d - 1);
            end = delta + (d - 1);
            for (int k = -d; k <= d; k += 2) {
                xf = findFurthestReachingDPath(d, k, a, n, b, m, vf);
                if (oddDelta && isIn(k, start, end)) {
                    if (xf > 0 && vb.get(k) <= xf) {
                        return findLastSnake(d, k, xf, a.zero(),b.zero(), vf, a, b);
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = findFurthestReachingDPathReverse(d, kk, a, n, b, m, delta, vb);
                if (!oddDelta && isIn(kk, -d, d)) {
                    if (xr >= 0 && xr <= vf.get(kk)) {
                        return findLastSnakeReverse(d, kk, xr, a.zero(),b.zero(), vb, delta, a, n, b, m);
                    }
                }
            }
        }
        return Snake.NULL;
    }

    private static boolean isIn(int value, int startInterval, int endInterval) {
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
            BidirectionalVector v,
            VList<T> a, VList<T> b) {
        int y = x - k;

        int xEnd = x;
        int yEnd = y;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        int xStart, yStart, xMid, yMid;
        if (k == -d) {
            xStart = next;
            yStart = next - k;
            xMid = xStart;
        } else if (k != d && prev < next) {
            xStart = next;
            yStart = next - k - 1;
            xMid = xStart;
        } else {
            xStart = prev;
            yStart = prev - k + 1;
            xMid = xStart + 1;
        }

        yMid = xMid - k;

        boolean reverse = false;
        if (xMid == xEnd && yMid == yEnd) {
            xMid = xStart;
            yMid = yStart;
            while (xStart >0 && yStart >0 && a.get(xStart).equals(b.get(yStart))) {
                xStart--;
                yStart--;
            }
            reverse = true;
        }

        return new Snake(reverse, d, x0+xStart, y0+yStart,
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
        if (x >= 0) {
            v.set(k, x);
        }
        return x;
    }

    private Snake findLastSnakeReverse(int d, int k, int xe, int x0, int y0,
            BidirectionalVector v, int delta,
            VList<T> a, int n, VList<T> b, int m) {
        final int xStart = xe;
        final int yStart = xe - k;
        int xEnd, yEnd, xMid, yMid;

        final int next = v.get(k + 1);
        final int prev = v.get(k - 1);
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

        boolean reverse = true;
        if (xMid == xStart && yMid == yStart) {
            xMid = xEnd;
            yMid = yEnd;
            while (xEnd < n && yEnd < m && a.get(xEnd+1).equals(b.get(yEnd+1))) {
                xEnd++;
                yEnd++;
            }
            reverse = false;
        }

        return new Snake(reverse, d, x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    /**
     * @return the common subsequence elements.
     */
    // TODO use a better visitor way to extract the list without the need to create a temporary list
    private List<T> extractLcs(Snake snakes, VList<T> a, VList<T> b) {
        System.out.println("SOLUTION of a: " + a.toString() + ", b: " + b.toString());
        List<T> list = new ArrayList<>();
        int x=0;
        boolean error=false;
        for (Snake snake : snakes) {
            if (snake.xStart != x) {
                error = true;
                System.out.println("ERROR");
            }
            System.out.println(snake);
            snake.addEquals(list, a);
            x = snake.xEnd;
        }
        if (error) {
            //throw new AssertionError("uncomplete chain");
        }
        return list;
    }

    /**
     * A snake is a sequence of equal
     * elements starting from mid to end and preceeded by a vertical or
     * horizontal edge going from start to mid.
     */
    static class Snake implements Iterable<Snake> {
        static final Snake NULL = new Snake(false, -1,-1,-1,-1,-1,-1,-1);
        public final int d, xStart, yStart, xMid, yMid, xEnd, yEnd;
        private final boolean reverse;
        private Snake next;

        public Snake(boolean reverse, int d,
                int xStart, int yStart, int xMid, int yMid, int xEnd, int yEnd) {
            this.reverse = reverse;
            this.d = d;
            this.xStart = xStart;
            this.yStart = yStart;
            this.xMid = xMid;
            this.yMid = yMid;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
//            System.out.println(this);
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

        public boolean isDiagonal() {
            if (reverse) {
                return xStart + 1 <= xMid;
            } else {
                return xMid + 1 <= xEnd;
            }
        }

        public <T> void addEquals(List<T> result, VList<T> a) {
            if (reverse) {
                for (int x=xStart + 1; x <= xMid; x++) {
                    result.add(a.get(x));
                }
            } else {
                for (int x = xMid + 1; x <= xEnd; x++) {
                    result.add(a.get(x));
                }
            }
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
            return "Snake{" + (reverse ? "reverse" : "forward") +
                    ", d=" + d +
                    ", xStart=" + xStart + ", yStart=" + yStart +
                    ", xMid=" + xMid + ", yMid=" + yMid + ", xEnd=" + xEnd +
                    ", yEnd=" + yEnd +
                    (isDiagonal() ? " diagonal " : "") + '}';
        }
    }
}
