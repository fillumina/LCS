package com.fillumina.lcs.hirschberg;

import java.util.List;
import java.util.Objects;
import java.util.AbstractList;
import java.util.Collections;
import com.fillumina.lcs.LcsList;

/**
 * Optimized Hirschberg Linear Space LCS algorithm that calculates the
 * reverse vector without the need to reverse the list.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedHirschbergLinearSpaceLcs implements LcsList {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        final int m = b.size();
        List<? extends T> list =
                lcsRlw(a, 0, a.size(), b, 0, m, new int[3][m + 1]);
        if (list == null) {
            return Collections.<T>emptyList();
        }
        return list;
    }

    <T> ConcatList<T> lcsRlw(
            List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        final int n = aEnd - aStart;

        switch (n) {
            case 0:
                return ConcatList.<T>empty();

            case 1:
                final T t = a.get(aStart);
                for (int i=bStart; i<bEnd; i++) {
                    if (Objects.equals(t, b.get(i))) {
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
            List<? extends T> a, int aStart, final int aBisect, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {

        int[] forward = calculateLcsForward(
                a, aStart, aBisect, b, bStart, bEnd, buffer);

        int[] backward = calculateLcsReverse(
                a, aBisect, aEnd, b, bStart, bEnd, buffer);

        return indexOfBiggerSum(forward, backward, bStart, bEnd) ;
    }

    private static <T> int[] calculateLcsForward(
            List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        int[] curr = buffer[0];
        int[] prev = buffer[2];
        int[] tmp;

        T x = a.get(aStart);
        curr[bStart] = 0;
        for (int i=bStart; i<bEnd; i++) {
            T y = b.get(i);
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
            x = a.get(j);
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                T y = b.get(i);
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
            List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        int[] curr = buffer[1];
        int[] prev = buffer[2];
        int[] tmp;
        int offset = bEnd + bStart - 1;

        T x = a.get(aEnd-1);
        curr[bStart] = 0;
        for (int i=bStart; i<bEnd; i++) {
            T y = b.get(offset - i);
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
            x = a.get(j);
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                T y = b.get(offset - i);
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

    /** A list optimized for concatenation. */
    static class ConcatList<T> extends AbstractList<T> {
        private static final ConcatList<?> EMPTY = new ConcatList();
        private T[] array;
        private int size;

        @SuppressWarnings("unchecked")
        static <T> ConcatList<T> empty() {
            return (ConcatList<T>) EMPTY;
        }

        /** Creates an empty list. */
        public ConcatList() {
        }

        /** Creates a one item list. */
        @SuppressWarnings("unchecked")
        public ConcatList(T t) {
            this.array = (T[]) new Object[]{t};
            size = 1;
        }

        @Override
        public T get(int index) {
            return array[index];
        }

        public ConcatList<T> concat(ConcatList<T> c) {
            if (c == null || c.size == 0) {
                return this;
            }
            if (size == 0) {
                return c;
            }
            final int newsize = size + c.size;
            if (array == null) {
                init(Math.max(10, newsize));
            } else if (newsize > array.length) {
                resize(newsize << 1);
            }
            System.arraycopy(c.array, 0, array, size, c.size);
            size = newsize;
            return this;
        }

        @SuppressWarnings("unchecked")
        private void init(final int length) {
            this.array = (T[]) new Object[length];
        }

        @SuppressWarnings("unchecked")
        private void resize(final int length) {
            Object[] tmp = new Object[length];
            System.arraycopy(array, 0, tmp, 0, size);
            this.array = (T[]) tmp;
        }

        @Override
        public int size() {
            return size;
        }
    }
}
