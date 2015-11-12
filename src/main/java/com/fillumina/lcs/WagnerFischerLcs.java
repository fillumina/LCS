package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;

/**
 * This algorithm uses a score table that needs to be initialized first and
 * it's read forward starting from the top-left (1975).
 * <p>
 * <img src="WagnerFisher.gif" />
 *
 * @see <a href="https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm">
 *  Wikipedia: Wagner-Fisher algorithm
 * </a>
 * @see <a href="http://stackoverflow.com/questions/30792428/wagner-fischer-algorithm">
 *  Stackoverflow: Wagner-Fisher algorithm
 * </a>
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WagnerFischerLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        int n = a.size();
        int m = b.size();

        int[][] d = computeDistance(a, n, b, m);

        return readLcs(n, m, d, a);
    }

    private int[][] computeDistance(List<T> a, int n, List<T> b, int m) {
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
                if (a.get(i-1).equals(b.get(j-1))) {
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

    private List<T> readLcs(int m, int n, int[][] d, List<T> s) {
        int lcsLength = d[m][n];
        List<T> lcs = new ArrayList<>(lcsLength);
        T se;
        // the table is read forward
        int i = 0, j = 0;
        while (i < m && j < n) {
            se = s.get(i);
            if (d[i][j] == d[i+1][j+1]) { // equals
                lcs.add(se);
                i++;
                j++;
            } else if (d[i+1][j] < d[i][j+1]) {
                i++;
            } else {
                j++;
            }
        }
        return lcs;
    }

    private int min(int a, int b, int c) {
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
