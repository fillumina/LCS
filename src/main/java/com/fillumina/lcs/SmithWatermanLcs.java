package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SmithWatermanLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(final List<T> a, final List<T> b) {
        final int n = a.size();
        final int m = b.size();
        final int[][] s = new int[n+1][m+1];

        // creates the grid
        s[0][0] = 0;
        for (int i=1; i<=n; i++) {
            for (int j=1; j<=m; j++) {
                if (Objects.equals(a.get(i-1), b.get(j-1))) {
                    s[i][j] = s[i-1][j-1] + 1;
                } else {
                    s[i][j] = Math.max(s[i-1][j], s[i][j-1]);
                }
            }
        }

        // read the path
        final int lcs = s[n][m];
        final List<T> lcsList = new ArrayList<>(lcs);
        int i=n, j=m;
        while (i>0 && j>0) {
            if (Objects.equals(a.get(i-1), b.get(j-1))) {
                i--;
                j--;
                lcsList.add(a.get(i));
            } else if (s[i-1][j] < s[i][j-1]) {
                j--;
            } else {
                i--;
            }
        }
        Collections.reverse(lcsList);
        return lcsList;
    }

}
