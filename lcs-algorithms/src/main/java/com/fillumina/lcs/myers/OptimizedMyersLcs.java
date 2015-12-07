package com.fillumina.lcs.myers;

import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import com.fillumina.lcs.Lcs;

/**
 * An optimization of the Myers algorithm.
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

        int size = (max << 1) + 1;
        int[][] vv = new int[max][size];
        int[] vNext, vPrev;

        int maxk, next, prev, x=-1, y, d, k=-1, s;
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
                while (x < n && y < m && Objects.equals(a[x], b[y])) {
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

        int xStart, xMid, index = max-1;
        for (; d >= 0 && x > 0; d--) {
            maxk = max + k;
            vNext = vv[d == 0 ? 0 : d-1];

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
                for (s=x-1; s>=xMid; s--) {
                    result[index] = a[s];
                    index--;
                }
            }

            x = xStart;
        }
        // the snakes are collected backwards
        List<T> list = Arrays.asList(result); //TODO use better code
        return list.subList(index+1, max);
    }
}
