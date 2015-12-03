package com.fillumina.lcs.docx4j;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
class IbmLcsLength extends LCS {

    private static final LCSSettings LCS_SETTINGS = new LCSSettings();
    private final Object[] a;
    private final Object[] b;
    private int lcs;

    public IbmLcsLength(
            List<?> a,
            List<?> b) {
        this.a = a.toArray(new Object[a.size()]);
        this.b = b.toArray(new Object[b.size()]);
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
