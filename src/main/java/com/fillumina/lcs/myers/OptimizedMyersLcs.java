package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.BidirectionalVector;
import com.fillumina.lcs.util.BidirectionalArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fillumina.lcs.ListLcs;
import java.util.Objects;

/**
 * An optimization of the Myers algorithm.
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
public class OptimizedMyersLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        @SuppressWarnings("unchecked")
        final T[] aa = a.toArray((T[])new Object[a.size()]);
        @SuppressWarnings("unchecked")
        final T[] bb = b.toArray((T[])new Object[b.size()]);
        final List<Snake> snakes = lcsMyers(aa, bb);
        return extractLcs(snakes, aa);
    }

    private List<Snake> lcsMyers(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;
        int max = n + m + 1;

        BidirectionalArray vv = new BidirectionalArray(max);
        BidirectionalVector v = new BidirectionalVector(max);

        v.set(1, 0);

        int next, prev, x, y;
        for (int d = 0; d < max; d++) {
            for (int k = -d; k <= d; k += 2) {
                next = v.get(k + 1); // down
                prev = v.get(k - 1); // right
                if (k == -d || (k != d && prev < next)) {
                    x = next;
                } else {
                    x = prev + 1;
                }
                y = x - k;
                while (x >= 0 && y >= 0 && x < n && y < m &&
                        Objects.equals(a[x], b[y])) {
                    x++;
                    y++;
                }
                v.set(k, x);
                if (x >= n && y >= m) {
                    // reached the end of the 'table'
                    if (d < max) {
                        vv.copyVectorOnLine(d, v);
                    }

                    return calculateSolution(d, vv, x, y);
                }
            }
            vv.copyVectorOnLine(d, v);
        }
        return Collections.<Snake>emptyList();
    }

    private List<Snake> calculateSolution(int lastD,
            BidirectionalArray vv, int xLast, int yLast) {
        List<Snake> snakes = new ArrayList<>();

        int x = xLast;
        int y = yLast;

        int d, xStart, yStart, xMid, yMid, xEnd, yEnd, next, prev;
        for (d = lastD; d >= 0 && x > 0 && y > 0; d--) {
            int k = x - y;

            xEnd = x;
            yEnd = y;

            next = vv.get(d, k + 1);
            prev = vv.get(d, k - 1);
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
        // the snakes are collected backwards
        Collections.reverse(snakes);
        return snakes;
    }

    /** @return the common subsequence elements. */
    private List<T> extractLcs(List<Snake> snakes, T[] a) {
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            for (int x=snake.xMid + 1; x<=snake.xEnd; x++) {
                list.add(a[x-1]);
            }
        }
        return list;
    }

    /**
     * Records each snake in the solution. A snake is a sequence of
     * equal elements starting from Mid to End and preceeded by a vertical
     * or horizontal edge going from Start to Mid.
     */
    protected static class Snake  {
        private Snake next;
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

}
