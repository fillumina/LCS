package com.fillumina.lcs.myers.docx4j;

import com.fillumina.lcs.Lcs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class IbmLcs<T> implements Lcs<T> {

    @Override
    @SuppressWarnings("unchecked")
    public List<T> lcs(List<T> xs, List<T> ys) {
        LcsImpl lcs = new LcsImpl(xs, ys);
        LCSSettings settings = new LCSSettings();
        lcs.longestCommonSubsequence(settings);
        return (List<T>) lcs.getSolution();
    }

    public static class LcsImpl extends LCS {

        private final List<?> a, b;
        private List<Integer> solutionIndexes;

        public LcsImpl(List<?> a, List<?> b) {
            this.a = a;
            this.b = b;
        }

        public List<?> getSolution() {
            if (solutionIndexes == null) {
                return Collections.EMPTY_LIST;
            }
            Collections.sort(solutionIndexes);
            List<Object> list = new ArrayList<>();
            for (int index : solutionIndexes) {
                list.add(a.get(index));
            }
            return list;
        }

        @Override
        protected int getLength2() {
            return b == null ? 0 : b.size();
        }

        @Override
        protected int getLength1() {
            return a == null ? 0 : a.size();
        }

        @Override
        protected boolean isRangeEqual(int i1, int i2) {
            if (a == null || b == null) {
                return false;
            }
            return a.get(i1).equals(b.get(i2));
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
