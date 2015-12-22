package com.fillumina.lcs.docx4j;

import java.util.Objects;

/**
 * Implementation of the IBM LCS algorithm optimized for calculating the LCS
 * length only.
 *
 * @author Francesco Illuminati
 */
class IbmLcsLength extends LCS {
    private static final LCSSettings LCS_SETTINGS = new LCSSettings();
    private final Object[] a;
    private final Object[] b;
    private int lcs;

    public IbmLcsLength(Object[] a, Object[] b) {
        this.a = a;
        this.b = b;
        longestCommonSubsequence(LCS_SETTINGS);
    }

    public int getLcs() {
        return lcs;
    }

    @Override
    protected int getLength2() {
        return b.length;
    }

    @Override
    protected int getLength1() {
        return a.length;
    }

    @Override
    protected boolean isRangeEqual(int i1, int i2) {
        return Objects.equals(a[i1], b[i2]);
    }

    @Override
    protected void setLcs(int sl1, int sl2) {
        lcs++;
    }

    @Override
    protected void initializeLcs(int lcsLength) {
    }

}
