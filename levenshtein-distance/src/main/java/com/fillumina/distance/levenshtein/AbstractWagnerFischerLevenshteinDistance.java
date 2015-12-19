package com.fillumina.distance.levenshtein;

/**
 * This algorithm, devised in 1975, uses a score table to calculate the
 * <a href='https://en.wikipedia.org/wiki/Levenshtein_distance'>
 * Levenshtein distance</a> between two sequences. The distance is
 * defined as the lowest number of editing operations (remove, add, substitute)
 * needed to transform one sequence into the other.
 * The operations are always symmetrical so the number of
 * operations are the same whatever is the source and the destination.
 * <p>
 * <img src="WagnerFisher.gif" />
 *
 * @see <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">
 *  Levenshtein distance
 * </a>
 * @see <a href="https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm">
 *  Wikipedia
 * </a>
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractWagnerFischerLevenshteinDistance {

    protected abstract int getFirstSequenceLength();

    protected abstract int getSecondSequenceLength();

    protected abstract boolean sameAtIndex(int x, int y);

    public <T> int distance() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        int[][] d = new int[n+1][m+1];

        // score table initialization
        for (int i=0; i<=n; i++) {
            // distance of any first string to an empty second string
            d[i][0] = i;
        }
        for (int j=0; j<=m; j++) {
            // distance of any second string to an empty first string
            d[0][j] = j;
        }

        // table creation
        for (int j=1; j<=m; j++) {
            for (int i=1; i<=n; i++) {
                // lists start from 0
                int ii = i-1;
                int jj = j-1;
                if (sameAtIndex(ii, jj)) {
                    d[i][j] = d[ii][jj];
                } else {
                    d[i][j] = 1 + min(
                            d[ii][j],      // deletion
                            d[i][jj],      // insertion
                            d[ii][jj]);    // substitution
                }
            }
        }
        return d[n][m];
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
