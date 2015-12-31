package com.fillumina.lcs;

/**
 * Very fast LCS implementation for small mostly different sequences
 * (of less than about 50 elements). In case of similar sequences the
 * simple Myers algorithm is about twice as fast.
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
public abstract class AbstractWagnerFischerLcs
        extends AbstractLcsHeadTailReducer {

    public AbstractWagnerFischerLcs() {
    }

    public AbstractWagnerFischerLcs(boolean sizeOnly) {
        super(sizeOnly);
    }

    @Override
    LcsItemImpl lcs(int a0, int n, int b0, int m) {
        int[][] d = computeDistanceMatrix(a0, n, b0, m);
        return backtrack(a0, n, b0, m, d);
    }

    private <T> int[][] computeDistanceMatrix(int a0, int n, int b0, int m) {
        int[][] d = createScoretable(n+1, m+1);

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
                if (sameAtIndex(a0 + i - 1, b0 + j - 1)) {
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

    /** Override to provide a scoretable {@code int[x][y]}. */
    protected int[][] createScoretable(int x, int y) {
        return new int[x][y];
    }

    private LcsItemImpl backtrack(int a0, int n, int b0, int m, int[][] d) {
        //int lcs = (n + m - d[n][m]) >> 1;
        LcsItemImpl tmp, current = null;
        int i = n, j = m;
        while (i>0 && j>0) {
            switch (minIndex(d[i-1][j-1], d[i-1][j], d[i][j-1])) {
                case 1:
                    if (d[i][j] == d[i-1][j-1]) {
                        tmp = match(a0 + i -1, b0 + j - 1, 1);
                        if (current == null) {
                            current = tmp;
                        } else {
                            current = tmp.chain(current);
                        }
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
        return current;
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
