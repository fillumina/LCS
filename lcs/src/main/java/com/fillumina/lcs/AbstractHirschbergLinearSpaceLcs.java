package com.fillumina.lcs;

/**
 * The Hirschberg linear space algorithm is very memory efficient (only
 * {@code 3 * (m+1)} int elements used) and fast for sequences which are
 * not similar. It can efficiently matches long sequences. In case the
 * sequences are known to be similar (6:4) the
 * {@link AbastractLinearSpaceMyersLcs} algorithm is more than twice faster.
 * To minimize the memory consumption set the smaller sequence to be the second
 * one.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Hirschberg's_algorithm'>
 *  Wikipedia: Hirschberg's Algorithm
 * </a>
 * @author Francesco Illuminati
 */
public abstract class AbstractHirschbergLinearSpaceLcs
        extends AbstractLcsHeadTailReducer {
    private int[][] array;

    public AbstractHirschbergLinearSpaceLcs() {
    }

    public AbstractHirschbergLinearSpaceLcs(boolean sizeOnly) {
        super(sizeOnly);
    }

    /** Override if you want to provide an array {@code int[3][m+1]}. */
    protected int[][] createArray(int m) {
        return new int[3][m+1];
    }

    @Override
    LcsItemImpl lcs(int aStart, int n, int bStart, int m) {
        if (array == null) {
            array = createArray(bStart+m);
        }
        int bEnd = bStart + m;
        int aEnd = aStart + n;

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
                        lcsHeadTail(aStart, aBisect-aStart, bStart, bBisect-bStart),
                        lcsHeadTail(aBisect, aEnd-aBisect, bBisect, bEnd-bBisect));
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
        int[] curr = array[0];
        int[] prev = array[2];
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
        if (curr == array[2]) {
            tmp = array[0];
            array[0] = array[2];
            array[2] = tmp;
        }
        return array[0];
    }

    private int[] calculateLcsReverse(
            int aStart, int aEnd,
            int bStart, int bEnd) {
        int[] curr = array[1];
        int[] prev = array[2];
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
        if (curr == array[2]) {
            tmp = array[1];
            array[1] = array[2];
            array[2] = tmp;
        }
        return array[1];
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
