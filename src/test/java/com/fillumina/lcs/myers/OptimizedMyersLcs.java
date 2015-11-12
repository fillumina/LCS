package com.fillumina.lcs.myers;

import java.util.List;
import com.fillumina.lcs.ListLcs;
import java.util.Arrays;
import java.util.Objects;

/**
 * An optimization of the Myers algorithm.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedMyersLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        @SuppressWarnings("unchecked")
        final T[] aa = a.toArray((T[])new Object[a.size()]);
        @SuppressWarnings("unchecked")
        final T[] bb = b.toArray((T[])new Object[b.size()]);
        return lcsMyers(aa, bb);
    }

    private List<T> lcsMyers(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;
        int max = n + m + 1;

        int size = (max << 1) + 1;
        int[][] vv = new int[max][size];
        int[] vNext, vPrev;

        int maxk, next, prev, x=-1, y=-1, d, k, s;
        FILL_THE_TABLE:
        for (d = 0; d < max; d++) {
            vPrev = vv[d == 0 ? 0 : d-1];
            vNext = vv[d];
            for (k = -d; k <= d; k += 2) {
                maxk = max + k;
                next = vPrev[maxk + 1]; // down
                prev = vPrev[maxk - 1]; // right
                if (k == -d || (k != d && prev < next)) {
                    x = next;
                } else {
                    x = prev + 1;
                }
                y = x - k;
                while (x >= 0 && y >= 0 && x < n && y < m &&
                        Objects.equals(a[x], b[y])) {
                    x++;
                    y++;
                }
                vNext[maxk] = x;
                if (x >= n && y >= m) {
                    break FILL_THE_TABLE;
                }
            }
        }

        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[max];

        int xStart, yStart, xMid, xEnd, v[], index = max-1;
        for (; d >= 0 && x > 0 && y > 0; d--) {
            k = x - y;
            maxk = max + k;
            v = vv[d == 0 ? 0 : d-1];

            xEnd = x;

            next = v[maxk + 1];
            prev = v[maxk - 1];
            if (k == -d || (k != d && prev < next)) {
                xStart = next;
                yStart = next - k - 1;
                xMid = xStart;
            } else {
                xStart = prev;
                yStart = prev - k + 1;
                xMid = xStart + 1;
            }

            if (xEnd != xMid) {
                for (s=xEnd-1; s>=xMid; s--) {
                    result[index] = a[s];
                    index--;
                }
            }

            x = xStart;
            y = yStart;
        }
        // the snakes are collected backwards
        List<T> list = Arrays.asList(result);
        return list.subList(index+1, max);
    }
}
