package com.fillumina.lcs.scoretable;

import java.util.List;
import com.fillumina.lcs.Lcs;
import java.util.Arrays;
import java.util.Objects;

/**
 * This algorithm is an adaptation from the original used to calculate the
 * Levenshtein distance. Because LCS doesn't account for substitutions the
 * correspondent code was left out. It builds a score table that needs to
 * be initialized first and then it is read backward to give the solution.
 *
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
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WagnerFischerLcs implements Lcs {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        int n = a.size();
        int m = b.size();

        int[][] d = computeDistanceMatrix(a, n, b, m);
//        System.out.println(ArrayPrinter.toString(a, b, d));
        return backtrack(d, n, m, a);
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
                    // modified to accomplish LCS in which the only operations
                    // are add and remove
                    d[i][j] = 1 + Math.min(d[i-1][j], d[i][j-1]);
                }
            }
        }
        return d;
    }

    private <T> List<? extends T> backtrack(int[][] d, int n, int m, List<T> a) {
        int lcs = (n + m - d[n][m]) >> 1;
        @SuppressWarnings("unchecked")
        T[] lcsSeq = (T[]) new Object[lcs];
        int i = n, j = m;
        while (i>0 && j>0) {
            switch (minIndex(d[i-1][j-1], d[i-1][j], d[i][j-1])) {
                case 1:
                    if (d[i][j] == d[i-1][j-1]) {
                        lcsSeq[--lcs] = a.get(i-1);
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
