package com.fillumina.lcs.docx4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.Lcs;

/**
 * This is an adapter to make the DOCX4J LCS algorithm be testable along
 * with the other algorithms in this project. This IBM implementation is a useful
 * reference for every speeding-up challenges. It implements the Myers
 * algorithm pretty efficiently and also can be limited on the maximum space
 * between successive matches (d) and a way to avoid calculating indexes
 * outside the virtual table.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class IbmLcs implements Lcs {
    private static final LCSSettings SETTINGS = new LCSSettings() {
        @Override public final boolean isUseGreedyMethod() { return false; }
        @Override public final double getPowLimit() { return 1.5; }
        @Override public final double getTooLong() { return Double.MAX_VALUE; }
    };

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        LcsImpl lcs = new LcsImpl(a, b);
        lcs.longestCommonSubsequence(SETTINGS);
        return (List<T>) lcs.getSolution();
    }

    /**
     * The IBM algorithm doesn't creates any structure to hold the LCS sequence
     * but delegates this operation to the implementor. Unfortunately the Myers
     * algorithm doesn't provide the indexes in a ordered way so if you need
     * them so you should sort them afterwards.
     */
    public static class LcsImpl extends LCS {

        private final Object[] a, b;
        private List<Integer> solutionIndexes;

        public LcsImpl(List<?> a, List<?> b) {
            this.a = a.toArray(new Object[a.size()]);
            this.b = b.toArray(new Object[b.size()]);
        }

        public List<?> getSolution() {
            if (solutionIndexes == null) {
                return Collections.EMPTY_LIST;
            }
            int size = solutionIndexes.size();
            Collections.sort(solutionIndexes);
            assert size == solutionIndexes.size();
            List<Object> list = new ArrayList<>(solutionIndexes.size());
            for (int i : solutionIndexes) {
                list.add(a[i]);
            }
            return list;
        }

        public int getLcs() {
            return solutionIndexes.size();
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
            solutionIndexes.add(sl1);
        }

        @Override
        protected void initializeLcs(int lcsLength) {
            solutionIndexes = new ArrayList<>(lcsLength);
        }
    }
}
