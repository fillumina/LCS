package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @see
 * <a href="http://www.codeproject.com/Articles/42279/Investigating-Myers-diff-algorithm-Part-of">
 * Investigating Myers' diff algorithm: Part 1 of 2
 * </a>
 * @see
 * <a href="http://www.codeproject.com/Articles/42280/Investigating-Myers-Diff-Algorithm-Part-of">
 * Investigating Myers' diff algorithm: Part 2 of 2
 * </a>
 * @see <a href="https://neil.fraser.name/software/diff_match_patch/myers.pdf">
 * An O(ND) Difference Algorithm and Its Variations, Myers 1986
 * </a>
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        return lcsMyers(new OneBasedVector<>(a), new OneBasedVector<>(b));
    }

    private List<T> lcsMyers(OneBasedVector<T> a, OneBasedVector<T> b) {
        int n = a.size();
        int m = b.size();
        int max = n + m;

        BidirectionalArray vv = new BidirectionalArray(max);
        BidirectionalVector v = new BidirectionalVector(max);

        v.set(1, 0);

        for (int d = 0; d <= max; d++) {
            for (int k = -d; k <= d; k += 2) {
                int next = v.get(k + 1);
                int prev = v.get(k - 1);
                int x, y;
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
                if (x >= n && y >= m) {
                    vv.copy(d, v);

                    int lcs = (max - d) / 2;
                    System.out.println(vv.toString());
                    System.out.println("L=" + lcs);
                    System.out.println("D=" + d);
                    List<Snake> snakes = calculateSolution(n, m, d, vv, x, y);
                    return extractLcs(snakes, a);
                }
            }
            vv.copy(d, v);
        }
        return Collections.<T>emptyList();
    }

    private List<Snake> calculateSolution(int n, int m, int lastD,
            BidirectionalArray vs, int xx, int yy) {
        List<Snake> snakes = new ArrayList<>();

        int x = xx;
        int y = yy;

        int d, xStart, yStart, xMid, yMid, xEnd, yEnd;
        for (d = lastD; d >= 0 && x > 0 && y > 0; d--) {
            int k = x - y;

            xEnd = x;
            yEnd = y;

            int next = vs.get(d - 1, k + 1);
            int prev = vs.get(d - 1, k - 1);
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

            snakes.add(0, new Snake(xStart, yStart, xMid, yMid, xEnd, yEnd));

            x = xStart;
            y = yStart;
        }
        return snakes;
    }

    private List<T> extractLcs(List<Snake> snakes, OneBasedVector<T> a) {
        System.out.println("EXTRACT");
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            System.out.println(snake);
            for (int x=snake.xMid + 1; x<=snake.xEnd; x++) {
                System.out.println("adding " + x);
                list.add(a.get(x));
            }
        }
        return list;
    }

    protected static class Snake {

        public final int xStart, yStart, xMid, yMid, xEnd, yEnd;

        public Snake(int xStart, int yStart, int xMid, int yMid, int xEnd,
                int yEnd) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xMid = xMid;
            this.yMid = yMid;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            System.out.println(toString());
        }

        @Override
        public String toString() {
            return "Snake{" + "xStart=" + xStart + ", yStart=" + yStart +
                    ", xMid=" + xMid + ", yMid=" + yMid + ", xEnd=" + xEnd +
                    ", yEnd=" + yEnd + '}';
        }
    }

    protected static class BidirectionalVector {

        private final int[] array;
        private final int halfSize;

        public BidirectionalVector(int[] array) {
            this.halfSize = array.length >> 1;
            this.array = array;
        }

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

    protected static class BidirectionalArray {

        private final int[][] array;
        private final int halfSize;

        public BidirectionalArray(int size) {
            this.halfSize = size;
            this.array = new int[halfSize][(halfSize << 1) + 1];
        }

        public int get(int x, int y) {
            if (x < 0 || x >= array.length) {
                return 0;
            }
            int indexY = halfSize + y;
            if (indexY < 0 || indexY >= array[x].length) {
                return 0;
            }
            return array[x][indexY];
        }

        public void set(int x, int y, int value) {
            if (x < 0 || x >= array.length) {
                return;
            }
            int indexY = halfSize + y;
            if (indexY < 0 || indexY >= array[x].length) {
                return;
            }
            array[x][indexY] = value;
        }

        public BidirectionalVector getVector(int x) {
            return new BidirectionalVector(array[x]);
        }

        public void copy(int line, BidirectionalVector v) {
            System.arraycopy(v.array, 0, array[line], 0, v.array.length);
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    buf.append(array[i][j]).append(" ");
                }
                buf.append('\n');
            }
            return buf.toString();
        }
    }

    protected static class OneBasedVector<T> {

        private final List<T> list;
        private final int size;

        public OneBasedVector(List<T> list) {
            this.list = list;
            this.size = list.size();
        }

        public T get(int x) {
            return list.get(x - 1);
        }

        public void set(int x, T value) {
            list.set(x - 1, value);
        }

        public int size() {
            return size;
        }
    }
}
