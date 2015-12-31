package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The Hirschberg linear space algorithm is very memory efficient (only
 * {@code 3 * (m+1)} int elements used) and fast for sequences which are
 * not similar. It can efficiently matches long sequences. In case the
 * sequences are known to be similar (6:4) the
 * {@link AbastractLinearSpaceMyersLcs} algorithm is more than twice faster.
 * To minimize the memory consumption set the smaller sequence to be the second
 * one.
 * 
 * @see <a href='https://en.wikipedia.org/wiki/Hirschberg's_algorithm'>
 *  Wikipedia: Hirschberg's Algorithm
 * </a>
 * @author Francesco Illuminati
 */
public class HirschbergLinearSpaceLcs implements Lcs {
    public static final HirschbergLinearSpaceLcs INSTANCE =
            new HirschbergLinearSpaceLcs();

    @Override
    public <T> List<T> calculateLcs(T[] a, T[] b) {
        final Inner<T> inner = new Inner<>(false, a, b);
        List<LcsItem> lcs = inner.calculateLcs();
        return inner.extractLcsList(lcs);
    }

    @Override
    public List<LcsItem> calculateLcsIndexes(Object[] a, Object[] b) {
        return new Inner<>(false, a, b).calculateLcs();
    }

    @Override
    public int calculateLcsLength(Object[] a, Object[] b) {
        return new Inner<>(true, a, b).calculateLcsLength();
    }

    private static class Inner<T> extends AbstractHirschbergLinearSpaceLcs {
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
                final Collection<? extends T> a,
                final Collection<? extends T> b) {
            this(sizeOnly, a.toArray(), b.toArray());
        }

        @SuppressWarnings("unchecked")
        public Inner(boolean sizeOnly, final Object[] a, final Object[] b) {
            super(sizeOnly);
            this.a = (T[]) a;
            this.b = (T[]) b;
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
