package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.VList;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.util.ListUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Myers algorithm that uses forward and backward snake. This is not designed
 * to be performant but to be easy to understand.
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
            return snake(false, a0, b0, a0, b0+m, a0, b0+m);
        }
        if (m == 0) {
            return snake(false, a0, b0, a0+n, b0, a0+n, b0);
        }

        Snake snake = findMiddleSnake(a, n, b, m);

        if (snake.xStart == a0 && snake.xEnd == (a0 + n) &&
                snake.yStart == b0 && snake.yEnd == (b0 + m)) {
            return snake;
        }

        Snake before =
            lcs(a.subList(1, snake.xStart + 1 - a0),
                    b.subList(1, snake.yStart + 1 - b0));
        Snake after =
            lcs(a.subList(snake.xEnd + 1 - a0, n + 1),
                    b.subList(snake.yEnd + 1 - b0, m + 1));

        return Snake.chain(before, snake, after);
    }

    Snake findMiddleSnake(VList<T> a, int n, VList<T> b, int m) {
        final int max = (int)Math.ceil((m + n)/2.0);
        final int delta = n - m;
        final boolean evenDelta = (delta & 1) == 0;

        final BidirectionalVector vf = new BidirectionalVector(max + 1);
        final BidirectionalVector vb = new BidirectionalVector(max + 1, delta);

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
                    return findLastSnakeReverse(d, kk, xr, a.zero(),b.zero(), vb, delta, a, n, b, m);
                }
            }
        }

        return nullSnake(a.zero(), b.zero(), n, m);
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
                a.get(x + 1).equals(b.get(y + 1))) {
            x++;
            y++;
        }
        vf.set(k, x);
        return x;
    }

    private Snake findLastSnake(int d, int k, int x, int x0, int y0,
            BidirectionalVector vf,
            VList<T> a, VList<T> b) {
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

        return snake(reverse, x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    private int findFurthestReachingDPathReverse(int d, int k,
            VList<T> a, int n, VList<T> b, int m,
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
                a.get(x).equals(b.get(y))) {
            x--;
            y--;
        }
        if (x >= 0) {
            vb.set(k, x);
        }
        return x;
    }

    private Snake findLastSnakeReverse(int d, int k, int xe, int x0, int y0,
            BidirectionalVector vb, int delta,
            VList<T> a, int n, VList<T> b, int m) {
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

        return snake(reverse, x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    /**
     * @return the common subsequence elements.
     */
    private List<T> extractLcs(Snake snakes, VList<T> a, VList<T> b) {
        List<T> list = new ArrayList<>();
        List<T> tmp = new ArrayList<>();
        int x=0;
        boolean error=false;
        for (Snake snake : snakes) {
//            System.out.print(snake);
            if (!snakeSet.remove(snake)) {
                throw new AssertionError("snake not present: " + snake.toString());
            }
            if (snake.xStart != x) {
                error = true;
            }
            tmp.clear();
            snake.addEquals(tmp, a);
//            System.out.println(" --> " + ListUtils.toString(tmp));
            list.addAll(tmp);
            x = snake.xEnd;
        }
        if (!snakeSet.isEmpty()) {
            throw new AssertionError("snakes not linked:\n" +
                    ListUtils.toString(snakeSet));
        }
        if (error) {
            throw new AssertionError("uncomplete chain:\n" +
                    ListUtils.toLines(snakes));
        }
        return list;
    }

    Set<Snake> snakeSet = new HashSet<>();
    private Snake snake(boolean reverse,
                int xStart, int yStart, int xMid, int yMid, int xEnd, int yEnd) {
        Snake s = new Snake(reverse, xStart, yStart, xMid, yMid, xEnd, yEnd);
        if (!snakeSet.add(s)) {
            throw new AssertionError("Snake already set: " + s.toString());
        }
        return s;
    }

    private Snake nullSnake(int xStart, int yStart, int n, int m) {
        final int xEnd = xStart + n;
        final int yEnd = yStart + m;
        return snake(false, xStart, yStart, xEnd, yEnd, xEnd, yEnd);
    }

    /**
     * A snake is a sequence of equal
     * elements starting from mid to end and preceeded by a vertical or
     * horizontal edge going from start to mid.
     */
    // TODO implement a reverse snake
    static class Snake implements Iterable<Snake> {
        public final int xStart, yStart, xMid, yMid, xEnd, yEnd;
        private final boolean reverse;
        private Snake next;

        private Snake(boolean reverse,
                int xStart, int yStart, int xMid, int yMid, int xEnd, int yEnd) {
            this.reverse = reverse;
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
            return "Snake{" + (reverse ? "reverse" : "forward") +
                    ", xStart=" + xStart + ", yStart=" + yStart +
                    ", xMid=" + xMid + ", yMid=" + yMid + ", xEnd=" + xEnd +
                    ", yEnd=" + yEnd +
                    (isDiagonal() ? " diagonal " : "") + '}';
        }
    }

    public static class BidirectionalVector {
        private final int[] array;
        private final int halfSize;

        public BidirectionalVector(int size) {
            this(size, 0);
        }

        /**
         * @param size specify the positive size (the total size will be
         *             {@code size * 2 + 1}.
         * @param constant is always subtracted to the given index
         */
        public BidirectionalVector(int size, int constant) {
            int length = size + Math.abs(constant);
            this.array = new int[(length << 1) + 1];
            this.halfSize = length - constant;
            Arrays.fill(array, -999);
        }

        public int get(int x) {
            int index = halfSize + x;
            if (index < 0 || index >= array.length) {
                throw new AssertionError(x);
            }
            return array[index];
        }

        public void set(int x, int value) {
            int index = halfSize + x;
            if (index < 0 || index >= array.length) {
                throw new AssertionError(x);
            }
            array[index] = value;
        }

        @Override
        public String toString() {
            return "" + halfSize + ":" + Arrays.toString(array);
        }

    }

}
