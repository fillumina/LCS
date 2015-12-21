package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs implements Lcs {
    public static final LinearSpaceMyersLcs INSTANCE =
            new LinearSpaceMyersLcs();

    @Override
    public <T> List<? extends T> calculateLcs(final List<? extends T> a,
            final List<? extends T> b) {
        final Inner<T> inner = new Inner<>(false, a, b);
        List<LcsItem> lcs = inner.calculateLcs();
        return inner.extractLcsList(lcs);
    }

    @Override
    public <T> List<LcsItem> calculateLcsIndexes(final List<? extends T> a,
            final List<? extends T> b) {
        return new Inner<>(false, a, b).calculateLcs();
    }

    @Override
    public <T> int calculateLcsLength(final List<? extends T> a,
            final List<? extends T> b) {
        return new Inner<>(true, a, b).calculateLcsLength();
    }

    private static class Inner<T> extends AbstractLinearSpaceMyersLcs {
        private final T[] a, b;

        /**
         * Performs the LCS calculation and return an object that can be
         * queried for the results.
         * @param <T> type of the lists
         * @param a   first sequence
         * @param b   second sequence
         * @return    result object that can be queried for various data
         */
        @SuppressWarnings("unchecked")
        public Inner(boolean sizeOnly,
                final List<? extends T> a,
                final List<? extends T> b) {
            super(sizeOnly);
            this.a = (T[]) a.toArray();
            this.b = (T[]) b.toArray();
        }

        @SuppressWarnings("unchecked")
        public List<T> extractLcsList(List<LcsItem> lcsItem) {
            if (lcsItem == null) {
                return Collections.<T>emptyList();
            }
            final List<T> lcs = new ArrayList<>(lcsItem.size());
            for (int index : lcsItem.get(0).lcsIndexesOfTheFirstSequence()) {
                lcs.add(a[index]);
            }
            return lcs;
        }

        @Override
        public final boolean sameAtIndex(final int i, final int j) {
            final T x = a[i];
            final T y = b[j];
            return (x == y) || (x != null && x.equals(y));
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
}
