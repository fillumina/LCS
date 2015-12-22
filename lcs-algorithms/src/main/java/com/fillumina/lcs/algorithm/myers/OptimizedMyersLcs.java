package com.fillumina.lcs.algorithm.myers;

import com.fillumina.lcs.helper.LcsList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * An optimization of the Myers algorithm that only copies the part of the
 * vector which was actually used. Comparing to other algorithms Myers' is fast
 * but uses a lot of memory especially if there are few matches.
 *
 * @author Francesco Illuminati
 */
public class OptimizedMyersLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;
        int max = n + m + 1;

        int[][] vv = new int[1 + (max >> 1)][];
        int[] v = new int[(max << 1) + 3];
        int[] tmp;

        int size, maxk, next, prev, x=-1, y, d, k=-1;
        for (d = 0; d < max; d++) {
            for (k = -d; k <= d; k += 2) {
                maxk = max + k;
                if (k == -d) {
                    x = v[maxk + 1];
                } else if (k == d) {
                    x = v[maxk - 1] + 1;
                } else {
                    next = v[maxk + 1]; // down
                    prev = v[maxk - 1]; // right
                    x = (prev < next) ? next : prev + 1;
                }
                y = x - k;
                while (x < n && y < m && Objects.equals(a[x], b[y])) {
                    x++;
                    y++;
                }
                v[maxk] = x;
                if (x >= n && y >= m) {
                    int dd = d + (d & 1);
                    size = (dd<<1) + 3;
                    tmp = new int[size + 2];
                    System.arraycopy(v, max-dd-1, tmp, 1, size);
                    vv[dd>>1] = tmp;

                    return backwardReadLcs(n, m, d, k, vv, x, a);
                }
            }
            if ((d & 1) == 0) {
                size = (d<<1) + 3;
                tmp = new int[size + 2];
                System.arraycopy(v, max-d-1, tmp, 1, size);
                vv[d>>1] = tmp;
            }
        }
        throw new AssertionError();
    }

    protected <T> List<T> backwardReadLcs(int n, int m, int d, int k,
            int[][] vv, int x, T[] a) {
        int maxk;
        int next;
        int prev;
        int xStart, xMid, index = (n + m - d) >> 1;
        @SuppressWarnings("unchecked")
                T[] result = (T[]) new Object[index];
        for (; d >= 0 && x > 0; d--) {
            int[] vNext = vv[d>>1];
            maxk = d + 2 + k -(d&1);

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
        return Arrays.asList(result);
    }
}
