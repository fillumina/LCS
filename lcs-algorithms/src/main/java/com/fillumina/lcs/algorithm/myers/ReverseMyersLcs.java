package com.fillumina.lcs.algorithm.myers;

import com.fillumina.lcs.helper.LcsList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Because the linear space optimization of the basic Myers algorithm uses
 * a reverse LCS this is an implementation of this variant. The algorithm is the
 * same as the normal Myers but it proceeds backwards.
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
 * @author Francesco Illuminati
 */
public class ReverseMyersLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        List<Snake> snakes = lcsMyers(a, b);
        return extractLcs(snakes, a);
    }

    private <T> List<Snake> lcsMyers(T[] a, T[] b) {
        final int n = a.length;
        final int m = b.length;
        if (n==0 || m==0) {
            return Collections.<Snake>emptyList();
        }

        final int dmax = n + m + 1;
        final int delta = n - m;

        BidirectionalVector[] vv = new BidirectionalVector[dmax];
        BidirectionalVector v = new BidirectionalVector(dmax, delta);

        v.set(delta-1, n);

        int next, prev, x, y;
        for (int d = 0; d < dmax; d++) {
            for (int k = -d+delta; k <= d+delta; k += 2) {
                next = v.get(k + 1);
                prev = v.get(k - 1);
                if (k == d+delta || (k != -d+delta && prev < next)) {
                    x = prev;       // up
                } else {
                    x = next - 1;   // left
                }
                y = x - k;
                while (x > 0 && y > 0 && Objects.equals(a[x - 1], b[y - 1])) {
                    x--;
                    y--;
                }
                v.set(k, x);
                if (x <= 0 && y <= 0) {
                    vv[d] = v.copy();

                    return calculateSolution(d, vv, x, y, delta, n, m);
                }
            }
            vv[d] = v.copy();
        }
        return Collections.<Snake>emptyList();
    }

    private List<Snake> calculateSolution(int lastD, BidirectionalVector[] vs,
            int xLast, int yLast, int delta, int n, int m) {
        List<Snake> snakes = new ArrayList<>();
        int d, next, prev, xMid, yMid, xEnd, yEnd;

        int xStart = xLast;
        int yStart = yLast;

        for (d = lastD; d >= 0 && xStart < n && yStart < m; d--) {
            int k = xStart - yStart;

            next = vs[d].get(k + 1);
            prev = vs[d].get(k - 1);
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

            snakes.add(new Snake(xStart, yStart, xMid, yMid));

            xStart = xEnd;
            yStart = yEnd;
        }
        return snakes;
    }

    /** @return the common subsequence elements. */
    private <T> List<T> extractLcs(List<Snake> snakes, T[] a) {
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            for (int x=snake.xStart + 1; x<=snake.xMid; x++) {
                list.add(a[x - 1]);
            }
        }
        return list;
    }

    /**
     * Records each snake in the solution. This is in fact a reverse
     * snake which differs from the forward one in the sense that the
     * sequence of equal elements goes from Start to Mid and the vertical
     * or horizontal edge goes from Mid to End.
     */
    static class Snake {

        public final int xStart, yStart, xMid, yMid;

        public Snake(int xStart, int yStart, int xMid, int yMid) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xMid = xMid;
            this.yMid = yMid;
        }

        @Override
        public String toString() {
            return "Snake{" + "xStart=" + xStart + ", yStart=" + yStart +
                    ", xMid=" + xMid + ", yMid=" + yMid + '}';
        }
    }
}
