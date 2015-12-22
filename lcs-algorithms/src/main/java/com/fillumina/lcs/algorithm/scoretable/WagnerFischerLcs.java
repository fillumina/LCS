package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.helper.LcsList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This algorithm builds a score table that needs to
 * be initialized first and then it is read backward to give the solution.
 * <p>
 * <img src="WagnerFisher.gif" />
 *
 * @see <a href="https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm">
 *  Wikipedia
 * </a>
 * @see <a href="http://stackoverflow.com/questions/30792428/wagner-fischer-algorithm">
 *  Stackoverflow
 * </a>
 * @see WagnerFisherLevenshteinDistance
 *
 * @author Francesco Illuminati
 */
public class WagnerFischerLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;

        int[][] d = computeDistanceMatrix(a, n, b, m);
        return backtrack(n, m, d, a);
    }

    private <T> int[][] computeDistanceMatrix(T[] a, int n, T[] b, int m) {
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
                if (Objects.equals(a[i-1], b[j-1])) {
                    d[i][j] = d[i-1][j-1];
                } else {
                    // modified to accomplish LCS in which the only operations
                    // are add and remove
                    d[i][j] = 1 + Math.min(d[i-1][j], d[i][j-1]);
                }
            }
        }
        return d;
    }

    private <T> List<T> backtrack(int n, int m, int[][] d, T[] a) {
        int lcs = (n + m - d[n][m]) >> 1;
        @SuppressWarnings("unchecked")
        T[] lcsSeq = (T[]) new Object[lcs];
        int i = n, j = m;
        while (i>0 && j>0) {
            switch (minIndex(d[i-1][j-1], d[i-1][j], d[i][j-1])) {
                case 1:
                    if (d[i][j] == d[i-1][j-1]) {
                        lcsSeq[--lcs] = a[i-1];
                    }
                    i--;
                    j--;
                    break;

                case 2:
                    i--;
                    break;

                case 3:
                    j--;
                    break;
            }
        }
        return Arrays.asList(lcsSeq);
    }

    static int minIndex(int a, int b, int c) {
        if (a < b) {
            if (c < a) {
                return 3;
            }
            return 1;
        } else {
            if (c < b) {
                return 3;
            }
            return 2;
        }
    }
}
