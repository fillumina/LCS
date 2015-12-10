package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.BidirectionalVector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.Lcs;

/**
 * Myers devises a faster way to perform the LCS by recursing the score table
 * along its diagonals. This means that his algorithm searches first
 * for matches along a specific distance and then just increases the allowed
 * distance until a match is found. The algorithm always work closer to the
 * optimal solution which is the diagonal (all elements are equal) and so the
 * first solution that completes can be safely taken.
 * The other algorithms starts by searching
 * a match from the farthest distance possible (because they always start from
 * the beginning of a row in the score table).
 * This implementation tries to be
 * as close as possible to the one described by the author on his paper.
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
public class MyersLcs implements Lcs {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        final List<Snake> snakes = lcsMyers(a, b);
        return extractLcs(snakes, a);
    }

    private <T> List<Snake> lcsMyers(List<? extends T> a,
            List<? extends T> b) {
        int n = a.size();
        int m = b.size();
        int max = n + m + 1;

        BidirectionalVector[] vv = new BidirectionalVector[max];
        BidirectionalVector v = new BidirectionalVector(max);

        int next, prev, x, y;
        for (int d = 0; d < max; d++) {
            for (int k = -d; k <= d; k += 2) {
                next = v.get(k + 1);
                prev = v.get(k - 1);
                if (k == -d || (k != d && prev < next)) {
                    x = next;       // down
                } else {
                    x = prev + 1;   // right
                }
                y = x - k;
                while (x < n && y < m && Objects.equals(a.get(x), b.get(y))) {
                    x++;
                    y++;
                }
                v.set(k, x);
                if (x >= n && y >= m) {
                    // reached the end of the 'table'
                    if (d < max) {
                        vv[d] = v.copy();
                    }

                    return calculateSolution(d, vv, x, y);
                }
            }
            vv[d] = v.copy();
        }
        return Collections.<Snake>emptyList();
    }

    private List<Snake> calculateSolution(int lastD,
            BidirectionalVector[] vv, int xLast, int yLast) {
        List<Snake> snakes = new ArrayList<>();
        int xStart, yStart, xMid, yMid, next, prev;

        int xEnd = xLast;
        int yEnd = yLast;

        for (int d = lastD; d >= 0 && xEnd > 0 && yEnd > 0; d--) {
            int k = xEnd - yEnd;

            next = vv[d].get(k + 1);
            prev = vv[d].get(k - 1);
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

            snakes.add(new Snake(xMid, yMid, xEnd, yEnd));

            xEnd = xStart;
            yEnd = yStart;
        }
        // the snakes are collected backwards
        Collections.reverse(snakes);
        return snakes;
    }

    /** @return the common subsequence elements. */
    private <T> List<? extends T> extractLcs(List<Snake> snakes,
            List<? extends T> a) {
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            for (int x=snake.xMid + 1; x<=snake.xEnd; x++) {
                list.add(a.get(x - 1));
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
        public final int xMid, yMid, xEnd, yEnd;

        public Snake(int xMid, int yMid, int xEnd, int yEnd) {
            this.xMid = xMid;
            this.yMid = yMid;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
        }

        @Override
        public String toString() {
            return "Snake{" +
                    ", xMid=" + xMid + ", yMid=" + yMid + ", xEnd=" + xEnd +
                    ", yEnd=" + yEnd + '}';
        }
    }

}
