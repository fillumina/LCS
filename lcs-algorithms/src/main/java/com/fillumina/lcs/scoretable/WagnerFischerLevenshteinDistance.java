package com.fillumina.lcs.scoretable;

import java.util.List;
import java.util.Objects;

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
public class WagnerFischerLevenshteinDistance {

    public <T> int distance(List<? extends T> a, List<? extends T> b) {
        int n = a.size();
        int m = b.size();

        int[][] d = computeDistanceMatrix(a, n, b, m);
        //System.out.println(ArrayPrinter.toString(a, b, d));
        return d[n][m];
    }

    private <T> int[][] computeDistanceMatrix(List<? extends T> a, int n,
            List<? extends T> b, int m) {
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
                if (Objects.equals(a.get(i-1), b.get(j-1))) {
                    d[i][j] = d[i-1][j-1];
                } else {
                    d[i][j] = 1 + min(
                            d[i-1][j],      // deletion
                            d[i][j-1],      // insertion
                            d[i-1][j-1]);   // substitution
                }
            }
        }
        return d;
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
