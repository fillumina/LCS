package com.fillumina.lcs.myers;

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

        int size = (max << 1) + 1;
        int[][] vv = new int[max][size];
        int[] vNext, vPrev;

        int maxk, next, prev, x, y;
        for (int d = 0; d < max; d++) {
            vPrev = vv[d == 0 ? 0 : d-1];
            vNext = vv[d];
            for (int k = -d; k <= d; k += 2) {
                maxk = max + k;
                next = vPrev[maxk + 1]; // down
                prev = vPrev[maxk - 1]; // right
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
                vNext[maxk] = x;
                if (x >= n && y >= m) {
                    return calculateSolution(d, vv, x, y, max);
                }
            }
        }
        return Collections.<Snake>emptyList();
    }

    private List<Snake> calculateSolution(int lastD,
            int[][] vv, int xLast, int yLast, int max) {
        List<Snake> snakes = new ArrayList<>();

        int x = xLast;
        int y = yLast;

        int d, xStart, yStart, xMid, steps, xEnd, next, prev, k, maxk, v[];
        for (d = lastD; d >= 0 && x > 0 && y > 0; d--) {
            k = x - y;
            maxk = max + k;
            v = vv[d == 0 ? 0 : d-1];

            xEnd = x;

            next = v[maxk + 1];
            prev = v[maxk - 1];
            if (k == -d || (k != d && prev < next)) {
                xStart = next;
                yStart = next - k - 1;
                xMid = xStart;
            } else {
                xStart = prev;
                yStart = prev - k + 1;
                xMid = xStart + 1;
            }

            steps = xEnd - xMid;
            if (steps != 0) {
                snakes.add(new Snake(xMid, xEnd));
            }

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
            for (int s=snake.xMid; s<snake.xEnd; s++) {
                list.add(a[s]);
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
        public final int xMid, xEnd;

        public Snake(int xMid, int xEnd) {
            this.xMid = xMid;
            this.xEnd = xEnd;
        }

        @Override
        public String toString() {
            return "Snake{" +
                    ", xMid=" + xMid +
                    ", xEnd=" + xEnd + '}';
        }
    }

}
