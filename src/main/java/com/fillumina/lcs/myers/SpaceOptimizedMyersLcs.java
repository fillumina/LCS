package com.fillumina.lcs.myers;

import com.fillumina.lcs.Lcs;
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
public class SpaceOptimizedMyersLcs<T> implements Lcs<T> {

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

        EndPointTable vv = new EndPointTable(max + 1);
        BidirectionalVector v = new BidirectionalVector(max);

        int next, prev, x, y;
        for (int d = 0; d <= max; d++) {
            for (int k = -d; k <= d; k += 2) {
                next = v.get(k + 1);
                prev = v.get(k - 1);
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

                    //int lcs = (max - d) / 2;
                    return calculateSolution(n, m, d, vv, x, y);
                }
            }
            vv.copy(d, v);
        }
        return Collections.<Snake>emptyList();
    }

    private List<Snake> calculateSolution(int n, int m, int lastD,
            EndPointTable vs, int xx, int yy) {
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

    /** Contains the k,d endpoints in a packed form. */
    protected static class EndPointTable {
        private final int[] vector;
        private final int[] precalculatedPlaces;
        private final int size;

        public EndPointTable(int size) {
            this.vector = new int[(size + 1) * size / 2 + 1];
            this.size = size;
            this.precalculatedPlaces = new int[size];
            for (int i=0; i<size; i++) {
                precalculatedPlaces[i] = (i + 1) * i / 2;
            }
        }

        public int get(int d, int k) {
            return vector[calculatePos(d, k)];
        }

        public void set(int d, int k, int value) {
            vector[calculatePos(d, k)] = value;
        }

        private int calculatePos(int d, int k) {
            if (d < 0 || d > size) {
                return vector.length - 1;
            }
            if (k < -size || k > size) {
                return vector.length - 1;
            }
            int kk = (k < 0) ? -k - 1 : k;
            return precalculatedPlaces[d] + kk;
        }

        public void copy(int d, BidirectionalVector v) {
            for (int k=-d; k<=d; k+=2) {
                set(d, k, v.get(k));
            }
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder("\n");
            for (int d=0; d<size; d++) {
                for (int k=-d; k<=d; k+=2) {
                    buf.append(get(d, k)).append(" ");
                }
                buf.append('\n');
            }
            return buf.toString();
        }

    }

    /** A vector that starts from index 1 instead of 0. */
    protected static class OneBasedVector<T> {
        private final List<T> list;

        public OneBasedVector(List<T> list) {
            this.list = list;
        }

        public T get(int x) {
            return list.get(x - 1);
        }

        public void set(int x, T value) {
            list.set(x - 1, value);
        }

        public int size() {
            return list.size();
        }
    }
}
