package com.fillumina.lcs;

import java.util.List;
import static com.fillumina.lcs.util.ListUtils.*;
import java.util.Collections;

/**
 *
 * @see <a href="https://www.ics.uci.edu/~eppstein/161/960229.html">
 *  LCS
 * </a>
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class HirschbergLinearSpaceAlgorithmLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
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
            List<T> xb = xs.subList(0, i);
            List<T> xe = xs.subList(i, nx);
            int[] ll_b = lcs_lens(xb, ys);
            int[] ll_e = lcs_lens(reverse(xe), reverse(ys));

            int k = indexOfBiggerSum(ll_b, ll_e, ny);

            List<T> yb = ys.subList(0, k);
            List<T> ye = ys.subList(k, ny);
            return concatenate(lcs(xb, yb), lcs(xe, ye));
        }
    }

    private int indexOfBiggerSum(int[] ll_b, int[] ll_e, int ny) {
        assert ny + 1 == ll_b.length && ny + 1 == ll_e.length;
        int tmp, k = -1, max = -1;
        for (int j=0; j<=ny; j++) {
            tmp = ll_b[j] + ll_e[ny-j];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }

    private int[] lcs_lens(List<T> xs, List<T> ys) {
        final int ysSize = ys.size();
        final int length = ysSize + 1;

        int[] curr = new int[length];
        int[] prev = new int[length];

        for (T x : xs) {
            System.arraycopy(curr, 0, prev, 0, length);

            for (int i=0; i<ysSize; i++) {
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
}
