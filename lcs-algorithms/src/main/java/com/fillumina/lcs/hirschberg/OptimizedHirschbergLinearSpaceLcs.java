package com.fillumina.lcs.hirschberg;

import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.Lcs;
import java.util.AbstractList;

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
        return lcsRlw(a, 0, a.size(), b, 0, m, new int[3][m + 1]);
    }

    <T> ArrayListImpl<T> lcsRlw(
            List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        final int n = aEnd - aStart;

        switch (n) {
            case 0:
                return new ArrayListImpl<>();

            case 1:
                final T t = a.get(aStart);
                for (int i=bStart; i<bEnd; i++) {
                    if (Objects.equals(t, b.get(i))) {
                        return new ArrayListImpl<>(t);
                    }
                }
                return new ArrayListImpl<>();

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
        int[] curr = zero(buffer[0], bStart, bEnd + 1);
        int[] prev = buffer[2];
        int[] tmp;

        for (int j=aStart; j<aEnd; j++) {
            T x = a.get(j);
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                T y = b.get(i);
                if (x.equals(y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        if (curr == buffer[2]) {
            System.arraycopy(buffer[2], bStart, buffer[0], bStart, bEnd - bStart + 1);
        }
        return buffer[0];
    }

    private static <T> int[] calculateLcsReverse(
            List<? extends T> a, int aStart, int aEnd,
            List<? extends T> b, int bStart, int bEnd,
            int[][] buffer) {
        int[] curr = zero(buffer[1], bStart, bEnd + 1);
        int[] prev = buffer[2];
        int[] tmp;

        for (int j=aEnd-1; j>=aStart; j--) {
            T x = a.get(j);
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                T y = b.get(bEnd - i + bStart - 1);
                if (x.equals(y)) {
                    curr[i + 1] = prev[i] + 1;
                } else {
                    curr[i + 1] = Math.max(curr[i], prev[i + 1]);
                }
            }
        }
        if (curr == buffer[2]) {
            System.arraycopy(buffer[2], bStart, buffer[1], bStart, bEnd - bStart + 1);
        }
        return buffer[1];
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

    static int[] zero(int[] array, int from, int to) {
        for (int i = from; i < to; i++) {
            array[i] = 0;
        }
        return array;
    }

    /** A list optimized for concatenation. */
    static class ArrayListImpl<T> extends AbstractList<T> {
        private T[] array;
        private int size;

        public ArrayListImpl() {
        }

        @SuppressWarnings("unchecked")
        public ArrayListImpl(T t) {
            this.array = (T[]) new Object[]{t};
            size = 1;
        }

        @Override
        public T get(int index) {
            return array[index];
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean add(T e) {
            if (array == null) {
                resize(10);
            }
            try {
                array[size++] = e;
                return true;
            } catch (IndexOutOfBoundsException ex) {
                resize(size << 1);
                array[size] = e;
                return true;
            }
        }

        public ArrayListImpl<T> concat(ArrayListImpl<T> c) {
            if (c.size == 0) {
                return this;
            }
            if (size == 0) {
                return c;
            }
            if (array == null || size + c.size > array.length) {
                resize(size + c.size);
            }
            System.arraycopy(c.array, 0, array, size, c.size);
            size += c.size;
            return this;
        }

        @SuppressWarnings("unchecked")
        private void resize(final int length) {
            if (this.array == null) {
                this.array = (T[]) new Object[length];
            } else {
                Object[] tmp = new Object[length];
                System.arraycopy(array, 0, tmp, 0, size);
                this.array = (T[]) tmp;
            }
        }

        @Override
        public int size() {
            return size;
        }
    }
}
