package com.fillumina.lcs.myers.docx4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.ListLcs;

/**
 * This is an adapter to make the DOCX4J LCS algorithm be testable along
 * with the other algorithms in this project. This IBM algorithm is a useful
 * reference for every speeding-up challenges.
 *
 * @see <a href='http://grepcode.com/file_/repo1.maven.org/maven2/org.docx4j/docx4j/3.2.1/org/eclipse/compare/internal/LCS.java/?v=source'>
 *  IBM Docx4J 3.2.1 LCS source</a>

 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class IbmLcs<T> implements ListLcs<T> {

    @Override
    @SuppressWarnings("unchecked")
    public List<T> lcs(List<T> xs, List<T> ys) {
        LcsImpl lcs = new LcsImpl(xs, ys);
        LCSSettings settings = new LCSSettings();
        lcs.longestCommonSubsequence(settings);
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
