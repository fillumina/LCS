package com.fillumina.lcs;

/**
 * Optimized Hirschberg Linear Space LCS algorithm that calculates the
 * reverse vector without the need to reverse the list.
 *
 * @author Francesco Illuminati
 */
public abstract class AbstractHirschbergLinearSpaceLcs
        extends AbstractLcsHeadTailReducer {
    private int[][] buffer;

    public AbstractHirschbergLinearSpaceLcs() {
    }

    public AbstractHirschbergLinearSpaceLcs(boolean sizeOnly) {
        super(sizeOnly);
    }

    @Override
    LcsItemImpl lcs(int a0, int n, int b0, int m) {
        buffer = createBuffer(b0+m);
        return lcsRlw(a0, a0+n, b0, b0+m);
    }

    /** Override if you want to provide a buffer. */
    protected int[][] createBuffer(int m) {
        return new int[3][m+1];
    }

    //TODO make it call the head-tail reducer in a loop
    LcsItemImpl lcsRlw(int aStart, int aEnd, int bStart, int bEnd) {
        final int n = aEnd - aStart;

        switch (n) {
            case 0:
                return null;

            case 1:
                for (int i=bStart; i<bEnd; i++) {
                    if (sameAtIndex(aStart, i)) {
                        return match(aStart, i, 1);
                    }
                }
                return null;

            default:
                final int aBisect = aStart + n / 2;

                final int bBisect = calculateCuttingIndex(
                        aStart, aBisect, aEnd,
                        bStart, bEnd);

                return concat(
                        lcsRlw(aStart, aBisect, bStart, bBisect),
                        lcsRlw(aBisect, aEnd, bBisect, bEnd));
        }
    }

    private LcsItemImpl concat(LcsItemImpl a, LcsItemImpl b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.chain(b);
    }

    private int calculateCuttingIndex(
            int aStart, int aBisect, int aEnd,
            int bStart, int bEnd) {

        int[] forward = calculateLcsForward(aStart, aBisect, bStart, bEnd);

        int[] backward = calculateLcsReverse(aBisect, aEnd, bStart, bEnd);

        return indexOfBiggerSum(forward, backward, bStart, bEnd) ;
    }

    private int[] calculateLcsForward(
            int aStart, int aEnd,
            int bStart, int bEnd) {
        int[] curr = buffer[0];
        int[] prev = buffer[2];
        int[] tmp;

        curr[bStart] = 0;
        for (int i=bStart; i<bEnd; i++) {
            if (sameAtIndex(aStart, i)) {
                for (int ii=i+1; ii<=bEnd; ii++) {
                    curr[ii] = 1;
                }
                break;
            } else {
                curr[i + 1] = 0;
            }
        }

        for (int j=aStart+1; j<aEnd; j++) {
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                if (sameAtIndex(j, i)) {
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

    private int[] calculateLcsReverse(
            int aStart, int aEnd,
            int bStart, int bEnd) {
        int[] curr = buffer[1];
        int[] prev = buffer[2];
        int[] tmp;
        int offset = bEnd + bStart - 1;

        curr[bStart] = 0;
        for (int i=bStart; i<bEnd; i++) {
            if (sameAtIndex(aEnd - 1, offset - i)) {
                for (int ii=i+1; ii<=bEnd; ii++) {
                    curr[ii] = 1;
                }
                break;
            } else {
                curr[i + 1] = 0;
            }
        }

        for (int j=aEnd-2; j>=aStart; j--) {
            tmp = prev;
            prev = curr;
            curr = tmp;
            curr[bStart] = 0;

            for (int i=bStart; i<bEnd; i++) {
                if (sameAtIndex(j, offset - i)) {
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
