package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Myers Linear LCS Algorithm for collections and arrays. This implementation
 * provides the LCS for collections and arrays.
 *
 * @see SequenceCreatorLcsInput
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CollectionLcsInput<T> implements LcsInput {
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
    public CollectionLcsInput(
            final Collection<? extends T> a,
            final Collection<? extends T> b) {
        this((T[]) a.toArray(), (T[]) b.toArray());
    }

    public CollectionLcsInput(T[] a, T[] b) {
        this.a = a;
        this.b = b;
    }

    @SuppressWarnings("unchecked")
    public List<T> extractLcsList(List<LcsItem> lcsItem) {
        final List<T> lcs = new ArrayList<>(lcsItem.size());
        for (int index : lcsItem.get(0).lcsIndexesOfTheFirstSequence()) {
            lcs.add(a[index]);
        }
        return lcs;
    }

    @Override
    public boolean equals(int x, int y) {
        return Objects.equals(a[x], b[y]);
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
