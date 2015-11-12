package com.fillumina.lcs.hirschberg;

import com.fillumina.lcs.hirschberg.*;
import com.fillumina.lcs.ListLcs;
import java.util.List;
import static com.fillumina.lcs.util.ListUtils.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Computes the LCS using linear space O(n).
 * The algorithms that use the LCS score table only work with 2
 * rows at a time (current row j and j-1) so it is possible to avoid
 * creating the entire table by just using these rows. By calculating
 * the forward and backward vector and watching where they meet the
 * LCS can be calculated by successively dividing the virtual score table at
 * the meeting point. The dividing technique is called
 * <a href='https://en.wikipedia.org/wiki/Dynamic_programming'>dynamic
 * programming</a>. This algorithm is slower than those that
 * use the full score table.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class HirschbergLinearSpaceAlgorithmLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        int n = a.size();
        int m = b.size();

        switch (n) {
            case 0:
                return Collections.<T>emptyList();

            case 1:
                final T t = a.get(0);
                if (b.contains(t)) {
                    return Collections.singletonList(t);
                }
                return Collections.<T>emptyList();

            default:
                int i = n / 2;
                List<T> aHead = a.subList(0, i);
                List<T> aTail = a.subList(i, n);
                int[] forward = calculateLcs(aHead, b);
                int[] backward = calculateLcs(reverse(aTail), reverse(b));

                int k = indexOfBiggerSum(forward, backward);

                List<T> bHead = b.subList(0, k);
                List<T> bTail = b.subList(k, m);
                return concatenate(lcs(aHead, bHead), lcs(aTail, bTail));
        }
    }

    private int indexOfBiggerSum(int[] fw, int[] bw) {
        int tmp, k = -1, max = -1, m = fw.length - 1;
        for (int j = 0; j <= m; j++) {
            tmp = fw[j] + bw[m - j];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }

    private int[] calculateLcs(List<T> a, List<T> b) {
        final int m = b.size();

        int[][] array = new int[2][m+1];
        int[] tmp;
        int[] curr = array[0];
        int[] prev = array[1];

        for (T x : a) {
            // swap(curr, prev)
            tmp = curr;
            curr = prev;
            prev = tmp;

            for (int i=0; i<m; i++) {
                T y = b.get(i);
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

    static <T> List<T> reverse(final List<T> list) {
        List<T> l = new ArrayList<>(list);
        Collections.reverse(l);
        return Collections.unmodifiableList(l);
    }
}
