package com.fillumina.lcs.myers;

import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import com.fillumina.lcs.Lcs;

/**
 * An optimization of the Myers algorithm that only copies the part of the
 * vector which was actually used thus avoiding to access main memory to
 * copy array elements which are not actually used by the algorithm.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedMyersLcs implements Lcs {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        @SuppressWarnings("unchecked")
        final T[] aa = a.toArray((T[])new Object[a.size()]);
        @SuppressWarnings("unchecked")
        final T[] bb = b.toArray((T[])new Object[b.size()]);
        return lcsMyers(aa, bb);
    }

    private <T> List<? extends T> lcsMyers(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;
        int max = n + m + 1;

        int[][] vv = new int[max][];
        int[] v = new int[(max << 1) + 3];

        int size, maxk, next, prev, x=-1, y, d, k=-1;
        FILL_THE_TABLE:
        for (d = 0; d < max; d++) {
            for (k = -d; k <= d; k += 2) {
                maxk = max + k;
                next = v[maxk + 1]; // down
                prev = v[maxk - 1]; // right
                if (k == -d || (k != d && prev < next)) {
                    x = next;
                } else {
                    x = prev + 1;
                }
                y = x - k;
                while (x < n && y < m && Objects.equals(a[x], b[y])) {
                    x++;
                    y++;
                }
                v[maxk] = x;
                if (x >= n && y >= m) {
                    size = (d<<1) + 3;
                    vv[d] = new int[size];
                    System.arraycopy(v, max-d-1, vv[d], 0, size);
                    break FILL_THE_TABLE;
                }
            }
            size = (d<<1) + 3;
            vv[d] = new int[size];
            System.arraycopy(v, max-d-1, vv[d], 0, size);
        }

        int xStart, xMid, index = (n + m - d) >> 1;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[index];

        for (; d >= 0 && x > 0; d--) {
            maxk = d + 1 + k;
            int[] vNext = vv[d];

            next = vNext[maxk + 1];
            prev = vNext[maxk - 1];
            if (k == -d || (k != d && prev < next)) {
                xStart = next;
                xMid = next;
                k++;
            } else {
                xStart = prev;
                xMid = prev + 1;
                k--;
            }

            if (x != xMid) {
                for (int s=x-1; s>=xMid; s--) {
                    index--;
                    result[index] = a[s];
                }
            }

            x = xStart;
        }
        // the snakes are collected backwards
        return Arrays.asList(result);
    }
}
