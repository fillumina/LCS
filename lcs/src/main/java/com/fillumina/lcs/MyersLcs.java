package com.fillumina.lcs;

/**
 * An implementation of the forward Myers algorithm. It's pretty fast
 * but uses an O(n^2) space.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcs extends LcsHeadTailReducer<Void> implements Lcs {
    public static final MyersLcs INSTANCE = new MyersLcs();

    @Override
    protected LcsItem lcs(Void obj,
            final LcsInput lcsInput,
            final LcsSequencer seqGen,
            final int a0, final int n,
            final int b0, final int m) {
        int max = n + m + 1;

        int[][] vv = new int[1 + (max >> 1)][];
        int[] v = new int[(max << 1) + 3];
        int[] tmpV;

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
                while (x < n && y < m && lcsInput.equals(a0 + x, b0 + y)) {
                    x++;
                    y++;
                }
                v[maxk] = x;
                if (x >= n && y >= m) {
                    int dd = d + (d & 1);
                    size = (dd<<1) + 3;
                    tmpV = new int[size + 2];
                    System.arraycopy(v, max-dd-1, tmpV, 1, size);
                    vv[dd>>1] = tmpV;

                    break FILL_THE_TABLE;
                }
            }
            if ((d & 1) == 0) {
                size = (d<<1) + 3;
                tmpV = new int[size + 2];
                System.arraycopy(v, max-d-1, tmpV, 1, size);
                vv[d>>1] = tmpV;
            }
        }

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
                LcsItemImpl tmp = new LcsItemImpl(
                        a0 + xMid, b0 + xMid - k, x - xMid);
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
