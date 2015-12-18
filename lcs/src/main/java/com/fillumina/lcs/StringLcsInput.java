package com.fillumina.lcs;

import java.util.List;

/**
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class StringLcsInput implements LcsInput {
    private final char[] a, b;

    /**
     * Performs the LCS calculation and return an object that can be
     * queried for the results.
     * @param <T> type of the lists
     * @param a   first sequence
     * @param b   second sequence
     * @return    result object that can be queried for various data
     */
    @SuppressWarnings("unchecked")
    public StringLcsInput(final String a, final String b) {
        this(a.toCharArray(), b.toCharArray());
    }

    public StringLcsInput(char[] a, char[] b) {
        this.a = a;
        this.b = b;
    }

    @SuppressWarnings("unchecked")
    public String extractLcsString(List<LcsItem> lcsItem) {
        return new String(extractLcsArray(lcsItem));
    }

    @SuppressWarnings("unchecked")
    public char[] extractLcsArray(List<LcsItem> lcsItem) {
        final char[] lcs = new char[lcsItem.size()];
        int counter = 0;
        for (int index : lcsItem.get(0).lcsIndexesOfTheFirstSequence()) {
            lcs[counter] = a[index];
            counter++;
        }
        return lcs;
    }

    @Override
    public final boolean equals(final int i, final int j) {
        return a[i] == b[i];
    }

    @Override
    public int getFirstSequenceLength() {
        return a == null ? 0 : a.length;
    }

    @Override
    public int getSecondSequenceLength() {
        return b == null ? 0 : b.length;
    }
}
