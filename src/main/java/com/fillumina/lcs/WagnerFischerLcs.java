package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;

/**
 * Note the string which is imagined to be kept at columns is constant and
 * we need to find the edit distance of the string kept at rows.
 * <br />
 * <img src="WagnerFisher.gif" />
 *
 * @see <a href="https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm">
 *  Wikipedia: Wagner-Fisher algorithm
 * </a>
 * @see <a href="http://stackoverflow.com/questions/30792428/wagner-fischer-algorithm">
 *  Stackoverflow: Wagner-Fisher algorithm
 * </a>
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WagnerFischerLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> s, List<T> t) {
        int m = s.size();
        int n = t.size();

        int[][] d = computeDistance(s, m, t, n);

        return createLcs(m, n, d, s);
    }

    private int[][] computeDistance(List<T> s, int m, List<T> t, int n) {
        int[][] d = new int[m+1][n+1];
        for (int i=0; i<=m; i++) {
            // distance of any first string to an empty second string
            d[i][0] = i;
        }
        for (int j=0; j<=n; j++) {
            // distance of any second string to an empty first string
            d[0][j] = j;
        }
        for (int j=1; j<=n; j++) {
            for (int i=1; i<=m; i++) {
                if (s.get(i-1).equals(t.get(j-1))) {
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

    private List<T> createLcs(int m, int n, int[][] d, List<T> s) {
        int lcsLength = d[m][n];
        List<T> lcs = new ArrayList<>(lcsLength);
        T se;
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
