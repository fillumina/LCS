package com.fillumina.distance;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class OptimizedAbstractHjelmqvistDistance {

    protected abstract int getFirstSequenceLength();

    protected abstract int getSecondSequenceLength();

    protected abstract boolean sameAtIndex(int x, int y);

    public <T> int distance() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();

        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }

        // create two work vectors of integer distances
        int[] v0 = new int[m + 1];
        int[] v1 = new int[m + 1];

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        for (int i = 0; i < n; i++) {
            // calculate v1 (current row distances) from the previous row v0

            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < m; j++) {
                int cost = sameAtIndex(i,j) ? 0 : 1;
                v1[j + 1] = min(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            System.arraycopy(v1, 0, v0, 0, v0.length);
        }

        return v1[m];
    }

    static int min(int a, int b, int c) {
        if (a < b) {
            if (c < a) {
                return c;
            }
            return a;
        } else {
            if (c < b) {
                return c;
            }
            return b;
        }
    }

}
