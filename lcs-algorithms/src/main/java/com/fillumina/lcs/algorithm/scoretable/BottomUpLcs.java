package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.helper.LcsList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This algorithm uses the LCS score table in a graphical way. It's useful
 * to visualize how a score table works.
 *
 * @see SmithWatermanLcs
 * @see WagnerFisherLcs
 *
 * @author Francesco Illuminati
 */
public class BottomUpLcs implements LcsList {
    private static final Cell DEFAULT_INVALID_CELL = new Cell(0, Move.INVALID);

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        final Grid grid = createGrid(a, b);
        return readLcs(a, b, grid);
    }

    private <T> Grid createGrid(T[] a, T[] b) {
        final int n = a.length;
        final int m = b.length;

        final Grid grid = new Grid(n, m, DEFAULT_INVALID_CELL);

        Cell cell;
        int left, over;

        for (int j = 0; j < m; j++) {
            T y = b[j];
            for (int i = 0; i < n; i++) {
                T x = a[i];

                if (Objects.equals(x, y)) {
                    cell = new Cell(grid.get(i - 1, j - 1).len + 1,
                            Move.DIAGONAL);

                } else {
                    left = grid.get(i, j - 1).len;
                    over = grid.get(i - 1, j).len;
                    if (left < over) {
                        cell = new Cell(over, Move.UP);
                    } else {
                        cell = new Cell(left, Move.LEFT);
                    }
                }

                grid.set(i, j, cell);
            }
        }
        return grid;
    }

    private <T> List<T> readLcs(T[] a, T[] b, Grid grid) {
        List<T> lcs = new ArrayList<>();
        int i = a.length - 1;
        int j = b.length - 1;
        Move move;
        do {
            move = grid.get(i, j).move;
            switch (move) {
                case DIAGONAL:
                    lcs.add(a[i]);
                    i--;
                    j--;
                    break;

                case UP:
                    i--;
                    break;

                case LEFT:
                    j--;
                    break;
            }
        } while (move != Move.INVALID);

        Collections.reverse(lcs);
        return lcs;
    }

    private static enum Move {
        UP('^'), LEFT('<'), DIAGONAL('\\'), INVALID(' ');

        private char symbol;

        Move(char symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return Character.toString(symbol);
        }
    }

    private static class Cell {
        private final int len;
        private final Move move;

        public Cell(int len, Move move) {
            this.len = len;
            this.move = move;
        }
    }

    /**
     * It's a matrix of {@link Cell}s that returns a default value when an
     * element has not been set or it's out of boundaries.
     */
    private static class Grid {

        private final Cell[][] array;
        private final Cell defaultValue;

        @SuppressWarnings("unchecked")
        public Grid(int rows, int columns, Cell defaultValue) {
            this.array = new Cell[rows][columns];
            this.defaultValue = defaultValue;
        }

        public Cell get(int x, int y) {
            Cell value;
            try {
                value = array[x][y];
            } catch (IndexOutOfBoundsException e) {
                return defaultValue;
            }
            if (value == null) {
                return defaultValue;
            }
            return value;
        }

        public void set(int x, int y, Cell cell) {
            if (cell == null) {
                return;
            }
            try {
                array[x][y] = cell;
            } catch (IndexOutOfBoundsException e) {
                // do nothing
            }
        }
    }

    /**
     * Produces a grid string representation. Useful for debugging.
     */
    static <T> String gridToString(
            List<? extends T> a, List<? extends T> b, Grid grid) {
        StringBuilder buf = new StringBuilder();
        buf.append("  ");
        for (int j = 0; j < b.size(); j++) {
            buf.append(b.get(j));
        }
        buf.append('\n');
        for (int i = 0; i < a.size(); i++) {
            buf.append(a.get(i)).append(' ');
            for (int j = 0; j < b.size(); j++) {
                buf.append(grid.get(i, j).move.toString());
            }
            buf.append('\n');
        }
        return buf.toString();
    }
}
