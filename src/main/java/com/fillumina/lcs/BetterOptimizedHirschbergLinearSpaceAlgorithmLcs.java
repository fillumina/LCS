package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BetterOptimizedHirschbergLinearSpaceAlgorithmLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        return lcs_rlw(new RList<>(xs), new RList<>(ys),
            new int[3][ys.size() + 1]);
    }

    List<T> lcs_rlw(RList<T> xs, RList<T> ys, int[][] buffer) {
        int nx = xs.size();
        int ny = ys.size();

        if (nx == 0) {
            return Collections.<T>emptyList();

        } else if (nx == 1) {
            final T xs0 = xs.get(0);
            if (ys.contains(xs0)) {
                return Collections.singletonList(xs0);
            }
            return Collections.<T>emptyList();

        } else {
            int i = nx / 2;
            final int start = ys.start();

            RList<T> xb = xs.subList(0, i);
            RList<T> xe = xs.subList(i, nx);

            int[] ll_b = lcs_lens(xb, ys, buffer, start);
            int[] ll_e = lcs_lens_reverse(xe, ys, buffer, start);

            int k = indexOfBiggerSum(ll_b, ll_e, start, ny);

            RList<T> yb = ys.subList(0, k);
            RList<T> ye = ys.subList(k, ny);

            // this step could be parallelized
            return add(lcs_rlw(xb, yb, buffer), lcs_rlw(xe, ye, buffer));
        }
    }

    private static int indexOfBiggerSum(int[] ll_b, int[] ll_e,
            int start, int ny) {
        //assert ny + 1 == ll_b.length && ny + 1 == ll_e.length;
        int tmp, k = -1, max = -1, end = start + ny;
        for (int j=start; j<=ny; j++) {
            tmp = ll_b[j] + ll_e[ny-j];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }

    private static <T> int[] lcs_lens(RList<T> xs, RList<T> ys,
            int[][] buffer, int start) {
        final int ysSize = ys.size();
        final int length = start + ysSize;

        int[] curr = buffer[0];
        Arrays.fill(curr, start, length, 0);
        int[] prev = buffer[2];

        for (T x : xs) {
            System.arraycopy(curr, start, prev, start, ysSize);

            for (int i=start; i<length; i++) {
                T y = ys.get(i - start);
                if (x.equals(y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        return curr;
    }

    private static <T> int[] lcs_lens_reverse(RList<T> xs, RList<T> ys,
            int[][] buffer, int start) {
        final int ysSize = ys.size();
        final int length = start + ysSize;

        int[] curr = buffer[1];
        Arrays.fill(curr, start, length, 0);
        int[] prev = buffer[2];

        for (int j=xs.size()-1; j>=0 ; j--) {
            T x = xs.get(j);
            System.arraycopy(curr, start, prev, start, ysSize);

            for (int i=length-1; i>start; i--) {
                T y = ys.get(i - start);
                if (x.equals(y)) {
                    curr[i - 1] = prev[i] + 1;
                } else {
                    curr[i - 1] = Math.max(curr[i], prev[i - 1]);
                }
            }
        }
        return curr;
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
