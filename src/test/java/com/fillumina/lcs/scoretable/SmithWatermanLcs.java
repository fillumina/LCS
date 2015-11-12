package com.fillumina.lcs.scoretable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.Lcs;

/**
 * Similar to the  Wagner-Fisher approach, it uses a score table that doesn't
 * need to be initialized first and so it's slightly more efficient.
 *
 * @see WagnerFisherLcs
 * @see <a href='https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm'>
 *  Wikipedia: Smith-Waterman Algorithm
 * </a>
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SmithWatermanLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        int n = a.size();
        int m = b.size();

        int[][] d = new int[n + 1][m + 1];

        // row 0 and column 0 are initialized to 0 already
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (Objects.equals(a.get(i), b.get(j))) {
                    d[i + 1][j + 1] = d[i][j] + 1;
                } else {
                    d[i + 1][j + 1]
                            = Math.max(d[i + 1][j], d[i][j + 1]);
                }
            }
        }

        int index = d[n][m];

        // read the substring out from the matrix
        @SuppressWarnings("unchecked")
        T[] sb = (T[]) new Object[index];
        index--;
        for (int x = n, y = m; x != 0 && y != 0;) {
            if (d[x][y] == d[x - 1][y]) {
                x--;
            } else if (d[x][y] == d[x][y - 1]) {
                y--;
            } else {
                sb[index--] = a.get(x - 1); // set the list in reverse order
                x--;
                y--;
            }
        }

        return Arrays.asList(sb);
    }

}
