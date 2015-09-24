package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BottomUpLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        Grid grid = createGrid(xs, ys);
        System.out.println(gridToString(xs, ys, grid));

        List<T> lcs = new ArrayList<>();
        int i = xs.size() - 1;
        int j = ys.size() - 1;
        char move;
        do {
            move = grid.get(i, j).move;
            System.out.println(i + " " + j + " " + move);
            switch(move) {
                case '\\':
                    lcs.add(xs.get(i));
                    i--;
                    j--;
                    break;

                case '^':
                    i--;
                    break;

                case '<':
                    j--;
                    break;
            }
        } while (move != 'e');

        Collections.reverse(lcs);
        return lcs;
    }

    private Grid createGrid(List<T> xs, List<T> ys) {
        final int xsSize = xs.size();
        final int ysSize = ys.size();

        final Grid grid = new Grid(xsSize, ysSize, new Cell(0, 'e'));

        Cell cell;
        int left, over;

        for (int j=0; j<ysSize; j++) {
            T y = ys.get(j);
            for (int i=0; i<xsSize; i++) {
                T x = xs.get(i);

                if (x.equals(y)) {
                    cell = new Cell(grid.get(i-1, j-1).len + 1, '\\');

                } else {
                    left = grid.get(i, j-1).len;
                    over = grid.get(i-1, j).len;
                    if (left < over) {
                        cell = new Cell(over, '^');
                    } else {
                        cell = new Cell(left, '<');
                    }
                }

                grid.set(i, j, cell);
            }
        }
        return grid;
    }

    private String gridToString(List<T> xs, List<T> ys, Grid grid) {
        StringBuilder buf = new StringBuilder();
        buf.append("  ");
        for (int j=0; j<ys.size(); j++) {
            buf.append(ys.get(j));
        }
        buf.append('\n');
        for (int i=0; i<xs.size(); i++) {
            buf.append(xs.get(i)).append(' ');
            for (int j=0; j<ys.size(); j++) {
                buf.append(grid.get(i,j).move);
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    private static class Cell {
        private final int len;
        private final char move;

        public Cell(int len, char move) {
            this.len = len;
            this.move = move;
        }
    }

    private static class Grid {
        private final Cell[][] array;
        private final Cell def;

        @SuppressWarnings("unchecked")
        public Grid(int maxX, int maxY, Cell def) {
            this.array = new Cell[maxX][maxY];
            this.def = def;
        }

        public Cell get(int x, int y) {
            Cell value;
            try {
                value = array[x][y];
            } catch (IndexOutOfBoundsException e) {
                value = def;
            }
            if (value == null) {
                return def;
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
}
