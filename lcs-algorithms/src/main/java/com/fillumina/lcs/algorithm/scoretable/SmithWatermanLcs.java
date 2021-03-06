package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.helper.LcsList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Similar to the  Wagner-Fisher approach, it uses a score table that doesn't
 * need to be initialized first.
 *
 * @see WagnerFisherLcs
 * @see <a href='https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm'>
 *  Wikipedia: Smith-Waterman Algorithm
 * </a>
 * @author Francesco Illuminati
 */
public class SmithWatermanLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        int n = a.length;
        int m = b.length;

        int[][] d = computeDistanceMatrix(a, n, b, m);
        return backtrack(n, m, d, a);
    }

    private <T> int[][] computeDistanceMatrix(T[] a, int n, T[] b, int m) {

        int[][] d = new int[n + 1][m + 1];

        // row 0 and column 0 are initialized to 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (Objects.equals(a[i], b[j])) {
                    d[i + 1][j + 1] = d[i][j] + 1;
                } else {
                    d[i + 1][j + 1] = Math.max(d[i + 1][j], d[i][j + 1]);
                }
            }
        }
        return d;
    }

    private <T> List<T> backtrack(int n, int m, int[][] d, T[] a) {
        int index = d[n][m]; // the lcs length

        @SuppressWarnings("unchecked")
        T[] sb = (T[]) new Object[index];
        index--; // last index
        for (int x = n, y = m; x != 0 && y != 0;) {
            if (d[x][y] == d[x - 1][y]) {
                x--;
            } else if (d[x][y] == d[x][y - 1]) {
                y--;
            } else {
                sb[index] = a[x - 1]; // set the list in reverse order
                index--;
                x--;
                y--;
            }
        }

        return Arrays.asList(sb);
    }

}
