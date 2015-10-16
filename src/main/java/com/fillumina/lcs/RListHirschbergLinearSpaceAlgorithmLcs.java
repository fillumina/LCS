package com.fillumina.lcs;

import com.fillumina.lcs.util.RList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RListHirschbergLinearSpaceAlgorithmLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        return lcs_rlw(new RList<>(xs), new RList<>(ys));
    }

    List<T> lcs_rlw(RList<T> xs, RList<T> ys) {
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

            RList<T> xb = xs.subList(0, i);
            RList<T> xe = xs.subList(i, nx);

            int[] ll_b = lcs_lens(xb, ys);
            int[] ll_e = lcs_lens(xe.reverse(), ys.reverse());

            int k = indexOfBiggerSum(ll_b, ll_e, ny);

            RList<T> yb = ys.subList(0, k);
            RList<T> ye = ys.subList(k, ny);

            // this step could be parallelized
            return add(lcs_rlw(xb, yb), lcs_rlw(xe, ye));
        }
    }

    private static int indexOfBiggerSum(int[] ll_b, int[] ll_e, int ny) {
        assert ny + 1 == ll_b.length && ny + 1 == ll_e.length;
        int tmp, k = -1, max = -1;
        for (int j = 0; j <= ny; j++) {
            tmp = ll_b[j] + ll_e[ny - j];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }

    private static <T> int[] lcs_lens(RList<T> xs, RList<T> ys) {
        final int ysSize = ys.size();
        final int length = ysSize + 1;

        int[] curr = new int[length];
        int[] prev = new int[length];

        for (T x : xs) {
            System.arraycopy(curr, 0, prev, 0, length);

            for (int i = 0; i < ysSize; i++) {
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
