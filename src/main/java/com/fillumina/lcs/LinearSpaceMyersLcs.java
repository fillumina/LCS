package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        final OneBasedVector<T> oneBasedVectorA = new OneBasedVector<>(a);
        List<Snake> snakes =
                lcsMyers(oneBasedVectorA, new OneBasedVector<>(b));
        return extractLcs(snakes, oneBasedVectorA);
    }

    private List<Snake> lcsMyers(OneBasedVector<T> a, OneBasedVector<T> b) {
        int n = a.size();
        int m = b.size();
        int max = n + m;

        BidirectionalArray vv = new BidirectionalArray(max);
        BidirectionalVector v = new BidirectionalVector(max);

        int[] xy = new int[2];
        for (int d = 0; d <= max; d++) {
            for (int k = -d; k <= d; k += 2) {
                findFurthestReachingDPath(d, k, a,n, b,m, v, xy);
                v.set(k, xy[0]);

                if (xy[0] >= n && xy[1] >= m) {
                    vv.copy(d, v);

                    //int lcs = (max - d) / 2;
                    return calculateSolution(n, m, d, vv, xy[0], xy[1]);
                }
            }
            vv.copy(d, v);
        }
        return Collections.<Snake>emptyList();
    }

    protected void findFurthestReachingDPath(int d, int k,
            OneBasedVector<T> a, int n, OneBasedVector<T> b, int m,
            BidirectionalVector v, int[] xy) {
        int x = xy[0];
        int y = xy[1];

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

        xy[0] = x;
        xy[1] = y;
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

            snakes.add(new Snake(xStart, yStart, xMid, yMid, xEnd, yEnd));

            x = xStart;
            y = yStart;
        }
        Collections.reverse(snakes);
        return snakes;
    }

    /** @return the common subsequence elements. */
    private List<T> extractLcs(List<Snake> snakes, OneBasedVector<T> a) {
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            System.out.println(snake);
            for (int x=snake.xMid + 1; x<=snake.xEnd; x++) {
                list.add(a.get(x));
            }
        }
        return list;
    }

    /**
     * Records each snake in the solution. A snake is a sequence of
     * equal elements starting from mid to end and preceeded by a vertical
     * or horizontal edge going from start to mid.
     */
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

    /** A vector that allows for negative indexes. */
    protected static class BidirectionalVector {

        private final int[] array;
        private final int halfSize;

        public BidirectionalVector(int[] array) {
            this.halfSize = array.length >> 1;
            this.array = array;
        }

        /**
         * @param size specify the positive size (the total size will be
         *             {@code size * 2 + 1}.
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

    /** An array that allows for negative indexes. */
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
            StringBuilder buf = new StringBuilder("\n");
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    buf.append(array[i][j]).append(" ");
                }
                buf.append('\n');
            }
            return buf.toString();
        }
    }

    public interface Vector<T> {
        T get(int x);
        Vector<T> reverse();
        void set(int x, T value);
        int size();
    }


    /** A vector that starts from index 1 instead of 0. */
    protected static class OneBasedVector<T> implements Vector<T> {
        private final List<T> list;

        public OneBasedVector(List<T> list) {
            this.list = list;
        }

        @Override
        public T get(int x) {
            return list.get(x - 1);
        }

        @Override
        public void set(int x, T value) {
            list.set(x - 1, value);
        }

        @Override
        public ReversedOneBasedVector<T> reverse() {
            return new ReversedOneBasedVector<>(list);
        }

        @Override
        public int size() {
            return list.size();
        }
    }

    /** A vector that starts from index 1 instead of 0. */
    protected static class ReversedOneBasedVector<T> implements Vector<T> {
        private final List<T> list;
        private final int size;

        public ReversedOneBasedVector(List<T> list) {
            this.list = list;
            this.size = list.size();
        }

        @Override
        public T get(int x) {
            return list.get(size - x);
        }

        @Override
        public void set(int x, T value) {
            list.set(size - x, value);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Vector<T> reverse() {
            return new OneBasedVector<>(list);
        }
    }

}
