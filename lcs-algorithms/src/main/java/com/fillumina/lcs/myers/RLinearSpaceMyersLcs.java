package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.BidirectionalVector;
import com.fillumina.lcs.util.VList;
import com.fillumina.lcs.util.ListUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
        final VList<? extends T> va = new VList<>(a);
        final VList<? extends T> vb = new VList<>(b);
        Snake snakes = lcs(va, vb);
        return extractLcs(snakes, va, vb);
    }

    <T> Snake lcs(VList<? extends T> a, VList<? extends T> b) {
        final int n = a.size();
        final int m = b.size();

        final int a0 = a.zero();
        final int b0 = b.zero();

        if (n == 0) {
            return new ForwardSnake(a0, b0, a0, b0+m, a0, b0+m);
        }
        if (m == 0) {
            return new ForwardSnake(a0, b0, a0+n, b0, a0+n, b0);
        }

        Snake snake = findMiddleSnake(a, n, b, m);

        if (snake.xStart == a0 && snake.xEnd == (a0 + n) &&
                snake.yStart == b0 && snake.yEnd == (b0 + m)) {
            return snake;
        }

        Snake before =
            lcs(a.subList(0, snake.xStart - a0),
                    b.subList(0, snake.yStart - b0));
        Snake after =
            lcs(a.subList(snake.xEnd - a0, n),
                    b.subList(snake.yEnd - b0, m));

        return Snake.chain(before, snake, after);
    }

    <T> Snake findMiddleSnake(VList<? extends T> a, int n,
            VList<? extends T> b, int m) {
        final int max = ((n + m + 1) >> 1) + 1; //(int)Math.ceil((m + n)/2.0);
        final int delta = n - m;
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
                xf = findFurthestReachingDPath(d, k, a, n, b, m, vf);
                if (!evenDelta && isIn(k, start, end) && vb.get(k) <= xf) {
                    return findLastSnake(d, k, xf, a.zero(),b.zero(), vf, a, b);
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = findFurthestReachingDPathReverse(d, kk, a, n, b, m, delta, vb);
                if (evenDelta && isIn(kk, -d, d) && xr >= 0 && xr <= vf.get(kk)) {
                    return findLastSnakeReverse(d, kk, xr, a.zero(),b.zero(),
                            vb, delta, a, n, b, m);
                }
            }
        }

        return new NullSnake(a.zero(), b.zero(), a.zero()+n, b.zero()+m);
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

    private <T> int findFurthestReachingDPath(int d, int k,
            VList<? extends T> a, int n, VList<? extends T> b, int m,
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
        while (x >= 0 && y >= 0 && x < n && y < m &&
                Objects.equals(a.get(x), b.get(y))) {
            x++;
            y++;
        }
        vf.set(k, x);
        return x;
    }

    private <T> Snake findLastSnake(int d, int k, int x, int x0, int y0,
            BidirectionalVector vf,
            VList<? extends T> a, VList<? extends T> b) {
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
                    a.get(xStart-1).equals(b.get(yStart-1))) {
                xStart--;
                yStart--;
            }
            return new ReverseSnake(x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
        }

        return new ForwardSnake(x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    private <T> int findFurthestReachingDPathReverse(int d, int k,
            VList<? extends T> a, int n, VList<? extends T> b, int m,
            int delta, BidirectionalVector vb) {
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
                Objects.equals(a.get(x-1), b.get(y-1))) {
            x--;
            y--;
        }
        if (x >= 0) {
            vb.set(k, x);
        }
        return x;
    }

    private <T> Snake findLastSnakeReverse(int d, int k, int xe, int x0, int y0,
            BidirectionalVector vb, int delta,
            VList<? extends T> a, int n, VList<? extends T> b, int m) {
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
            while (xEnd < n && yEnd < m && a.get(xEnd).equals(b.get(yEnd))) {
                xEnd++;
                yEnd++;
            }
            return new ForwardSnake(x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
        }

        return new ReverseSnake(x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    /**
     * @return the common subsequence elements.
     */
    private <T> List<? extends T> extractLcs(Snake snakes,
            VList<? extends T> a, VList<? extends T> b) {
        List<T> list = new ArrayList<>();
        List<T> tmp = new ArrayList<>();
        int x=0;
        boolean error=false;
        for (Snake snake : snakes) {
            if (snake.xStart != x) {
                error = true;
            }
            tmp.clear();
            snake.addEquals(tmp, a);
            list.addAll(tmp);
            x = snake.xEnd;
        }
        if (error) {
            throw new AssertionError("uncomplete chain:\n" +
                    ListUtils.toLines(snakes));
        }
        return list;
    }

    public static abstract class Snake implements Iterable<Snake> {
        public final int xStart, yStart;
        public final int xMid, yMid;
        public final int xEnd, yEnd;
        private Snake next;

        private Snake(
                int xStart, int yStart, int xMid, int yMid, int xEnd, int yEnd) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xMid = xMid;
            this.yMid = yMid;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
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

        abstract public <T> void addEquals(List<? super T> result, VList<T> a);

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

    private static class NullSnake extends Snake {

        public NullSnake(int xStart, int yStart, int xEnd, int yEnd) {
            super(xStart, yStart, xStart, yStart, xEnd, yEnd);
        }

        @Override
        public <T> void addEquals(List<? super T> result, VList<T> a) {
            // do nothing
        }
    }

    private static class ForwardSnake extends Snake {

        public ForwardSnake(int xStart, int yStart, int xMid,
                int yMid, int xEnd, int yEnd) {
            super(xStart, yStart, xMid, yMid, xEnd, yEnd);
        }

        @Override
        public <T> void addEquals(List<? super T> result, VList<T> a) {
            for (int x = xMid + 1; x <= xEnd; x++) {
                result.add(a.get(x-1));
            }
        }
    }

    private static class ReverseSnake extends Snake {

        public ReverseSnake(int xStart, int yStart, int xMid,
                int yMid, int xEnd, int yEnd) {
            super(xStart, yStart, xMid, yMid, xEnd, yEnd);
        }

        @Override
        public <T> void addEquals(List<? super T> result, VList<T> a) {
            for (int x=xStart + 1; x <= xMid; x++) {
                result.add(a.get(x-1));
            }
        }

    }
}
