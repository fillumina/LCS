package com.fillumina.lcs;

/**
 * An implementation of the forward Myers algorithm. It's pretty fast
 * but uses an O(n^2) space.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcs<T> extends LcsHeadTailReducer<T, Void> {
    public static final MyersLcs<?> INSTANCE = new MyersLcs<>();

    @Override
    LcsItemImpl lcs(Void obj,
            final T[] a, final int a0, final int n,
            final T[] b, final int b0, final int m) {
        int max = n + m + 1;

        int[][] vv = new int[1 + (max >> 1)][];
        int[] v = new int[(max << 1) + 1];
        int[] tmpV;

        int maxk, x, y, next, prev;
        for (int d = 0; d < max; d++) {
            for (int k = -d; k <= d; k += 2) {
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
                while (x < n && y < m && same(a[a0 + x], b[b0 + y])) {
                    x++;
                    y++;
                }
                v[maxk] = x;

                if (x >= n && y >= m) {
                    int dd = d + (d & 1);
                    int size = (dd<<1) + 3;
                    tmpV = new int[size + 2];
                    System.arraycopy(v, max-dd-1, tmpV, 1, size);
                    vv[dd>>1] = tmpV;

                    return extractLcs(vv, a0, b0, d, k, x);
                }
            }
            if ((d & 1) == 0) {
                int size = (d<<1) + 3;
                tmpV = new int[size + 2];
                System.arraycopy(v, max-d-1, tmpV, 1, size);
                vv[d>>1] = tmpV;
            }
        }
        throw new AssertionError();
    }

    private LcsItemImpl extractLcs(final int[][] vv, final int a0, final int b0,
            int d, int k, int x) {
        int maxk;
        int next;
        int prev;
        int xStart, xMid;
        LcsItemImpl head=null;
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
                LcsItemImpl tmp = (LcsItemImpl)
                        match(a0 + xMid, b0 + xMid - k, x - xMid);
                if (head == null) {
                    head = tmp;
                } else {
                    head = tmp.chain(head);
                }
            }
            x = xStart;
        }
        return head;
    }
}
