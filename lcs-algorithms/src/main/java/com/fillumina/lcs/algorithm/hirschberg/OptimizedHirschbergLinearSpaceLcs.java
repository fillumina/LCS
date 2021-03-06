package com.fillumina.lcs.algorithm.hirschberg;

import com.fillumina.lcs.helper.LcsList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Optimized Hirschberg Linear Space LCS algorithm that calculates the
 * reverse vector without the need to reverse the list.
 *
 * @author Francesco Illuminati
 */
public class OptimizedHirschbergLinearSpaceLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        final int m = b.length;
        List<T> list = lcsRlw(a, 0, a.length, b, 0, m, new int[3][m + 1]);
        if (list == null) {
            return Collections.<T>emptyList();
        }
        return list;
    }

    <T> ConcatList<T> lcsRlw(
            T[] a, int aStart, int aEnd,
            T[] b, int bStart, int bEnd,
            int[][] buffer) {
        final int n = aEnd - aStart;

        switch (n) {
            case 0:
                return ConcatList.<T>empty();

            case 1:
                final T t = a[aStart];
                for (int i=bStart; i<bEnd; i++) {
                    if (Objects.equals(t, b[i])) {
                        return new ConcatList<>(t);
                    }
                }
                return ConcatList.<T>empty();

            default:
                final int aBisect = aStart + n / 2;

                final int bBisect = calculateCuttingIndex(
                        a, aStart, aBisect, aEnd,
                        b, bStart, bEnd,
                        buffer);

                return lcsRlw(a, aStart, aBisect, b, bStart, bBisect, buffer)
                    .concat(lcsRlw(a, aBisect, aEnd, b, bBisect, bEnd, buffer));
        }
    }

    private <T> int calculateCuttingIndex(
            T[] a, int aStart, final int aBisect, int aEnd,
            T[] b, int bStart, int bEnd,
            int[][] buffer) {

        int[] forward = calculateLcsForward(
                a, aStart, aBisect, b, bStart, bEnd, buffer);

        int[] backward = calculateLcsReverse(
                a, aBisect, aEnd, b, bStart, bEnd, buffer);

        return indexOfBiggerSum(forward, backward, bStart, bEnd) ;
    }

    private static <T> int[] calculateLcsForward(
            T[] a, int aStart, int aEnd,
            T[] b, int bStart, int bEnd,
            int[][] buffer) {
        int[] curr = buffer[0];
        int[] prev = buffer[2];
        int[] tmp;

        T x = a[aStart];
        curr[bStart] = 0;
        for (int i=bStart; i<bEnd; i++) {
            T y = b[i];
            if (Objects.equals(x, y)) {
                for (int ii=i+1; ii<=bEnd; ii++) {
                    curr[ii] = 1;
                }
                break;
            } else {
                curr[i + 1] = 0;
            }
        }

        for (int j=aStart+1; j<aEnd; j++) {
            x = a[j];
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                T y = b[i];
                if (Objects.equals(x, y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }

        // swap the buffers so that curr is always buffer[0]
        if (curr == buffer[2]) {
            tmp = buffer[0];
            buffer[0] = buffer[2];
            buffer[2] = tmp;
        }
        return buffer[0];
    }

    private static <T> int[] calculateLcsReverse(
            T[] a, int aStart, int aEnd,
            T[] b, int bStart, int bEnd,
            int[][] buffer) {
        int[] curr = buffer[1];
        int[] prev = buffer[2];
        int[] tmp;
        int offset = bEnd + bStart - 1;

        T x = a[aEnd-1];
        curr[bStart] = 0;
        for (int i=bStart; i<bEnd; i++) {
            T y = b[offset - i];
            if (Objects.equals(x, y)) {
                for (int ii=i+1; ii<=bEnd; ii++) {
                    curr[ii] = 1;
                }
                break;
            } else {
                curr[i + 1] = 0;
            }
        }

        for (int j=aEnd-2; j>=aStart; j--) {
            x = a[j];
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                T y = b[offset - i];
                if (Objects.equals(x, y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }

        // swap the buffers so that curr is always buffer[1]
        if (curr == buffer[2]) {
            tmp = buffer[1];
            buffer[1] = buffer[2];
            buffer[2] = tmp;
        }
        return buffer[1];
    }

    private static int indexOfBiggerSum(int[] forward, int[] reverse,
            int start, int end) {
        int tmp, k = -1, max = -1, offset = end + start;
        for (int j=start; j<=end; j++) {
            tmp = forward[j] + reverse[offset - j];
            if (tmp > max) {
                max = tmp;
                k = j;
            }
        }
        return k;
    }
}
