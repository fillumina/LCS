package com.fillumina.lcs.algorithm.hirschberg;

import com.fillumina.lcs.helper.LcsList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Computes the LCS using linear space.
 * The algorithms that use the LCS score table only works with 2
 * rows at a time (current row j and j-1) so it is possible to avoid
 * creating the entire table by just using these two. By calculating
 * the forward and backward vector and watching where they meet the
 * LCS can be calculated by successively dividing the virtual score table at
 * meeting points. The dividing technique is called
 * <a href='https://en.wikipedia.org/wiki/Dynamic_programming'>dynamic
 * programming</a>. This algorithm is slower than those that
 * use the full score table.
 *
 * @author Francesco Illuminati
 */
public class HirschbergLinearSpaceAlgorithmLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;

        switch (n) {
            case 0:
                return Collections.<T>emptyList();

            case 1:
                final T t = a[0];
                for (int i=0; i<m; i++) {
                    if (b[i] == t) {
                        return Collections.singletonList(t);
                    }
                }
                return Collections.<T>emptyList();

            default:
                int i = n / 2;
                T[] aHead = Arrays.copyOfRange(a, 0, i);
                T[] aTail = Arrays.copyOfRange(a, i, n);
                int[] forward = calculateLcs(aHead, b);
                int[] backward = calculateLcs(reverse(aTail), reverse(b));

                int k = indexOfBiggerSum(forward, backward);

                T[] bHead = Arrays.copyOfRange(b, 0, k);
                T[] bTail = Arrays.copyOfRange(b, k, m);
                return concatenate(lcs(aHead, bHead), lcs(aTail, bTail));
        }
    }

    /** Returns the index in which the two LCS meet. */
    private int indexOfBiggerSum(int[] forward, int[] backward) {
        int tmp, k = -1, max = -1, m = forward.length - 1;
        for (int j = 0; j <= m; j++) {
            tmp = forward[j] + backward[m - j];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }

    /**
     * Uses the Smith-Waterman algorithm to calculate the score table
     * (or distance table) using only 2 rows.
     * @see com.fillumina.lcs.algorithm.scoretable.SmithWatermanLcs
     * @return the last row of the score table
     */
    private <T> int[] calculateLcs(T[] a, T[] b) {
        final int m = b.length;

        int[][] array = new int[2][m+1];
        int[] curr = array[0];
        int[] prev = array[1];
        int[] tmp;

        for (T x : a) {
            // swap(curr, prev)
            tmp = curr;
            curr = prev;
            prev = tmp;

            // it's the Smith-Waterman algorithm
            for (int i=0; i<m; i++) {
                T y = b[i];
                if (Objects.equals(x, y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        return curr;
    }

    /** The given lists are not modified. */
    static <T> List<T> concatenate(List<T> a, List<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        List<T> l = new ArrayList<>(a.size() + b.size());
        l.addAll(a);
        l.addAll(b);
        return l;
    }

    /** The given array is not modified. */
    static <T> T[] reverse(final T[] array) {
        final int length = array.length;
        @SuppressWarnings("unchecked")
        T[] reversed = (T[]) new Object[length];
        for (int i=0; i<length; i++) {
            reversed[i] = array[length - i - 1];
        }
        return reversed;
    }
}
