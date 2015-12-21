package com.fillumina.lcs.scoretable;

import java.util.List;

/**
 *
 * @author Francesco Illuminati 
 */
public class ScoreTablePrinter {

    public static <T> String toString(List<? extends T> a,
            List<? extends T> b,
            int[][] scoreTable) {
        StringBuilder buf = new StringBuilder();
        buf.append(" \t\t");
        for (int i=0; i<b.size(); i++) {
            buf.append('\t').append(b.get(i));
        }
        buf.append('\n');
        for (int i=0; i<scoreTable.length; i++) {
            buf.append(i == 0 ? " " : a.get(i-1));
            buf.append('\t');
            final int[] row = scoreTable[i];
            for (int j=0; j<row.length; j++) {
                buf.append('\t').append(row[j]);
            }
            buf.append('\n');
        }
        return buf.toString();
    }
}
