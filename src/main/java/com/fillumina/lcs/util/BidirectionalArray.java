package com.fillumina.lcs.util;

import java.util.Arrays;


/** An array that allows for negative indexes (on the 'y' index). */
public class BidirectionalArray {
    private final int[][] array;
    private final int halfSize;

    public BidirectionalArray(int size) {
        this.halfSize = size;
        this.array = new int[halfSize][(halfSize << 1) + 1];
    }

    public int get(int x, int y) {
        if (x < 0 || x >= array.length) {
            throw new IllegalArgumentException(
                    "first index out of boudaries (max=" + halfSize + "): " + x);
        }
        int indexY = halfSize + y;
        if (indexY < 0 || indexY >= array[x].length) {
            return 0;
        }
        return array[x][indexY];
    }

    public void set(int x, int y, int value) {
        if (x < 0 || x >= array.length) {
            return;
        }
        int indexY = halfSize + y;
        if (indexY < 0 || indexY >= array[x].length) {
            return;
        }
        array[x][indexY] = value;
    }

    public BidirectionalVector getVector(int x) {
        return new BidirectionalVector(array[x]);
    }

    public void copy(int line, BidirectionalVector v) {
        v.copyToArray(this.array[line]);
    }

    @Override
    public String toString() {
        return printBidirectionalArray(array);
    }

    public static String printBidirectionalArray(int[][] array) {
        StringBuilder buf = new StringBuilder("\n      ");
        final int l = array[0].length;
        for (int i=0; i<l; i++) {
            buf.append(pad(3, "" + (i - l/2)));
        }
        buf.append("\n    +").append(repeat(3 * (l + 1),'-')).append('\n');
        for (int i=0; i<array.length; i++) {
            buf.append(pad(3, "" + i)).append(" | ");
            for (int j=0; j<array[i].length; j++) {
                buf.append(pad(3, "" + array[i][j]));
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    private static String pad(int size, String s) {
        final int l = s.length();
        if (l >= size) {
            return s;
        }
        return repeat(size - l, ' ') + s;
    }

    private static String repeat(int number, char c) {
        char[] a = new char[number];
        Arrays.fill(a, c);
        return new String(a);
    }
}
