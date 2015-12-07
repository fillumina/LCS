package com.fillumina.lcs.util;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ArrayPrinter {

    public static <T> String toString(List<? extends T> x,
            List<? extends T> y,
            int[][] a) {
        StringBuilder buf = new StringBuilder();
        buf.append(" \t\t");
        for (int i=0; i<y.size(); i++) {
            buf.append('\t').append(y.get(i));
        }
        buf.append('\n');
        for (int i=0; i<a.length; i++) {
            buf.append(i == 0 ? " " : x.get(i-1));
            buf.append('\t');
            final int[] row = a[i];
            for (int j=0; j<row.length; j++) {
                buf.append('\t').append(row[j]);
            }
            buf.append('\n');
        }
        return buf.toString();
    }
}
