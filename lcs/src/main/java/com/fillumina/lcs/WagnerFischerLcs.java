package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Very fast LCS implementation for small mostly different sequences
 * (of less than about 50 elements). In case of similar sequences the
 * not linear Myers algorithm is about twice as fast.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm">
 *  Wikipedia
 * </a>
 * @see <a href="http://stackoverflow.com/questions/30792428/wagner-fischer-algorithm">
 *  Stackoverflow
 * </a>
 * @see WagnerFisherLevenshteinDistance
 *
 * @author Francesco Illuminati
 */
public class WagnerFischerLcs implements Lcs {
    public static final WagnerFischerLcs INSTANCE =
            new WagnerFischerLcs();

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

    private static class Inner<T> extends AbstractWagnerFischerLcs {
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
