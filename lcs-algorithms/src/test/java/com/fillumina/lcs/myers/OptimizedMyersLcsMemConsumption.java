package com.fillumina.lcs.myers;

import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import com.fillumina.lcs.helper.LcsList;

/**
 * Algorithm used for memory accounting.
 * An optimization of the Myers algorithm that only copies the part of the
 * vector which was actually used.
 *
 * @author Francesco Illuminati 
 */
public class OptimizedMyersLcsMemConsumption implements LcsList {
    private int memCounter;

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        @SuppressWarnings("unchecked")
        final T[] aa = a.toArray((T[])new Object[a.size()]);
        @SuppressWarnings("unchecked")
        final T[] bb = b.toArray((T[])new Object[b.size()]);
        return lcsMyers(aa, bb);
    }

    public int getMemCounter() {
        return memCounter;
    }

    private <T> List<? extends T> lcsMyers(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;
        int max = n + m + 1;

        int[][] vv = new int[1 + (max >> 1)][];
        memCounter += 1 + (max >> 1);

        int[] v = new int[(max << 1) + 3];
        memCounter += (max << 1) + 3;
        int[] tmp;

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
                    int dd = d + (d & 1);
                    size = (dd<<1) + 3;

                    tmp = new int[size + 2];
                    memCounter += size + 2;

                    System.arraycopy(v, max-dd-1, tmp, 1, size);
                    vv[dd>>1] = tmp;

                    break FILL_THE_TABLE;
                }
            }
            if ((d & 1) == 0) {
                size = (d<<1) + 3;

                tmp = new int[size + 2];
                memCounter += size + 2;

                System.arraycopy(v, max-d-1, tmp, 1, size);
                vv[d>>1] = tmp;
            }
        }

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
