package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BetterOptimizedHirschbergLinearSpaceAlgorithmLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        final int ysSize = ys.size();
        return lcs_rlw(xs, 0, xs.size(),
                ys, 0, ysSize,
            new int[3][ysSize + 1]);
    }

    List<T> lcs_rlw(List<T> xs, int xsStart, int xsEnd,
            List<T> ys, int ysStart, int ysEnd,
            int[][] buffer) {
        final int xsSize = xsEnd - xsStart;

        if (xsSize == 0) {
            return Collections.<T>emptyList();

        } else if (xsSize == 1) {
            final T xs0 = xs.get(xsStart);
            for (int i=ysStart; i<ysEnd; i++) {
                if (xs0.equals(ys.get(i))) {
                    return Collections.singletonList(xs0);
                }
            }
            return Collections.<T>emptyList();

        } else {
            final int i = xsStart + xsSize / 2;

            int[] ll_b = lcs_lens(xs, xsStart, i, ys, ysStart, ysEnd, buffer);
            int[] ll_e = lcs_lens_reverse(xs, i, xsEnd, ys, ysStart, ysEnd, buffer);

            int k = indexOfBiggerSum(ll_b, ll_e, ysStart, ysEnd) ;

            return add(lcs_rlw(xs, xsStart, i, ys, ysStart, k, buffer),
                    lcs_rlw(xs, i, xsEnd, ys, k, ysEnd, buffer));
        }
    }

    private static int indexOfBiggerSum(int[] ll_b, int[] ll_e,
            int start, int end) {
        int tmp, k = -1, max = -1;
        for (int j=start; j<=end; j++) {
            tmp = ll_b[j] + ll_e[end-j+start];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }

    private static <T> int[] lcs_lens(List<T> xs, int xsStart, int xsEnd,
            List<T> ys, int ysStart, int ysEnd,
            int[][] buffer) {
        int[] curr = buffer[0];
        zero(curr, ysStart, ysEnd + 1);
        int[] prev = buffer[2];

        for (int j=xsStart; j<xsEnd; j++) {
            T x = xs.get(j);
            System.arraycopy(curr, ysStart, prev, ysStart, ysEnd - ysStart + 1);

            for (int i=ysStart; i<ysEnd; i++) {
                T y = ys.get(i);
                if (x.equals(y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        return curr;
    }

    private static <T> int[] lcs_lens_reverse(
            List<T> xs, int xsStart, int xsEnd,
            List<T> ys, int ysStart, int ysEnd,
            int[][] buffer) {
        int[] curr = buffer[1];
        zero(curr, ysStart, ysEnd + 1);
        int[] prev = buffer[2];

        for (int j=xsEnd-1; j>=xsStart; j--) {
            T x = xs.get(j);
            System.arraycopy(curr, ysStart, prev, ysStart, ysEnd - ysStart + 1);

            for (int i=ysStart; i<ysEnd; i++) {
                T y = ys.get(ysEnd - i + ysStart - 1);
                if (x.equals(y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        return curr;
    }

    private static void zero(int[] array, int from, int to) {
        for (int i = from; i < to; i++) {
            array[i] = 0;
        }
    }

    private static <T> List<T> add(List<T> a, List<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        try {
            a.addAll(b);
            return a;
        } catch (UnsupportedOperationException e) {
            List<T> l = new ArrayList<>(a);
            l.addAll(b);
            return l;
        }
    }
}
