package com.fillumina.lcs.hirschberg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.Lcs;

/**
 * Optimized Hirschberg Linear Space LCS algorithm that calculates the
 * reverse vector without the need to reverse the list.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedHirschbergLinearSpaceLcs implements Lcs {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        final int m = b.size();
        return lcsRlw(a, 0, a.size(), b, 0, m,
            new int[3][m + 1]);
    }

    <T> List<? extends T> lcsRlw(List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        final int n = aEnd - aStart;

        switch (n) {
            case 0:
                return Collections.<T>emptyList();

            case 1:
                final T t = a.get(aStart);
                for (int i=bStart; i<bEnd; i++) {
                    if (Objects.equals(t, b.get(i))) {
                        return Collections.singletonList(t);
                    }
                }
                return Collections.<T>emptyList();

            default:
                final int i = aStart + n / 2;

                final int k = calculateCuttingIndex(a, aStart, i, aEnd,
                        b, bStart, bEnd, buffer);

                return add(lcsRlw(a, aStart, i, b, bStart, k, buffer),
                        lcsRlw(a, i, aEnd, b, k, bEnd, buffer));
        }
    }

    private <T> int calculateCuttingIndex(
            List<? extends T> a, int aStart, final int i, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        int[] forward = calculateLcsForward(a, aStart, i, b, bStart, bEnd, buffer);
        int[] backward = calculateLcsReverse(a, i, aEnd, b, bStart, bEnd, buffer);
        return indexOfBiggerSum(forward, backward, bStart, bEnd) ;
    }

    private static <T> int[] calculateLcsForward(
            List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        int[] curr = buffer[0];
        zero(curr, bStart, bEnd + 1);
        int[] prev = buffer[2];

        for (int j=aStart; j<aEnd; j++) {
            T x = a.get(j);
            System.arraycopy(curr, bStart, prev, bStart, bEnd - bStart + 1);

            for (int i=bStart; i<bEnd; i++) {
                T y = b.get(i);
                if (x.equals(y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        return curr;
    }

    private static <T> int[] calculateLcsReverse(
            List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        int[] curr = buffer[1];
        zero(curr, bStart, bEnd + 1);
        int[] prev = buffer[2];

        for (int j=aEnd-1; j>=aStart; j--) {
            T x = a.get(j);
            System.arraycopy(curr, bStart, prev, bStart, bEnd - bStart + 1);

            for (int i=bStart; i<bEnd; i++) {
                T y = b.get(bEnd - i + bStart - 1);
                if (x.equals(y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        return curr;
    }

    private static int indexOfBiggerSum(int[] forward, int[] reverse,
            int start, int end) {
        int tmp, k = -1, max = -1;
        for (int j=start; j<=end; j++) {
            tmp = forward[j] + reverse[end-j+start];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }

    static void zero(int[] array, int from, int to) {
        for (int i = from; i < to; i++) {
            array[i] = 0;
        }
    }

    static <T> List<? extends T> add(List<? extends T> a, List<? extends T> b) {
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
}
