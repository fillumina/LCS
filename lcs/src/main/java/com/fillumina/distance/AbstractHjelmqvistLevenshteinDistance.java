package com.fillumina.distance;

/**
 * A Levenshtein algorithm using only 2 rows of the distance matrix. It's
 * very fast and memory efficient.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">
 *  Levenshtein distance
 * </a>
 * @see <a href='http://www.codeproject.com/Articles/13525/Fast-memory-efficient-Levenshtein-algorithm'>
 *  Fast memory efficient Levenshtein algorithm (Sten Hjelmqvist)
 * </a>
 *
 * @author Francesco Illuminati 
 */
public abstract class AbstractHjelmqvistLevenshteinDistance {

    /** @return the length of the first sequence. */
    protected abstract int getFirstSequenceLength();

    /** @return the length of the second sequence. */
    protected abstract int getSecondSequenceLength();

    /** @return {@code true} when the elements at the specified indexes
     *          matches.
     */
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

            // it would seem better to simply swap the arrays or use
            // System.arraycopy(), but this method, althought slow in
            // theory, allows a better use of cache and it's way faster
            // than the others. (see tests)
            for (int j = 0; j < v0.length; j++) {
                v0[j] = v1[j];
            }
        }

        return v1[m];
    }

    static int min(int a, int b, int c) {
        int mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;
    }
}
