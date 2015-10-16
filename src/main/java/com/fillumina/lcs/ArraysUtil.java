package com.fillumina.lcs;

import java.util.Arrays;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ArraysUtil {

    public static String printVector(int[] vector) {
        return printArray(new int[][]{vector});
    }

    public static String printArray(int[][] array) {
        StringBuilder buf = new StringBuilder("\n      ");
        final int l = array[0].length;
        for (int i=0; i<l; i++) {
            buf.append(equalize(3, "" + (i - l/2)));
        }
        buf.append("\n    +").append(repeat(3 * (l + 1),'-')).append('\n');
        for (int i=0; i<array.length; i++) {
            buf.append(equalize(3, "" + i)).append(" | ");
            for (int j=0; j<array[i].length; j++) {
                buf.append(equalize(3, "" + array[i][j]));
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    public static String equalize(int size, String s) {
        final int l = s.length();
        if (l >= size) {
            return s;
        }
        return repeat(size - l, ' ') + s;
    }

    public static String repeat(int number, char c) {
        char[] a = new char[number];
        Arrays.fill(a, c);
        return new String(a);
    }


}
