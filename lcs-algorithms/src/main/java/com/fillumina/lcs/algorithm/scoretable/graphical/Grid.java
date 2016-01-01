/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fillumina.lcs.algorithm.scoretable.graphical;

/**
 * It's a matrix of {@link Cell}s that returns a default value when an
 * element has not been set or it's out of boundaries.
 */
class Grid {

    private final Cell[][] array;
    private final Cell defaultValue;

    @SuppressWarnings(value = "unchecked")
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
