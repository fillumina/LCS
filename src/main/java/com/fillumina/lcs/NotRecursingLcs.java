package com.fillumina.lcs;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class NotRecursingLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        int al = a.size();
        int bl = b.size();

        int[][] lengths = new int[al + 1][bl + 1];

        // row 0 and column 0 are initialized to 0 already
        for (int i = 0; i < al; i++) {
            for (int j = 0; j < bl; j++) {
                if (a.get(i).equals(b.get(j))) {
                    lengths[i + 1][j + 1] = lengths[i][j] + 1;
                } else {
                    lengths[i + 1][j + 1]
                            = Math.max(lengths[i + 1][j], lengths[i][j + 1]);
                }
            }
        }

        int index = lengths[al][bl];

        // read the substring out from the matrix
        @SuppressWarnings("unchecked")
        T[] sb = (T[]) new Object[index];
        index--;
        for (int x = al, y = bl;
                x != 0 && y != 0;) {
            if (lengths[x][y] == lengths[x - 1][y]) {
                x--;
            } else if (lengths[x][y] == lengths[x][y - 1]) {
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
