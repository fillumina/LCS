package com.fillumina.lcs.helper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Francesco Illuminati 
 */
public abstract class AbstractLcsSizeEvaluatorAdaptor
        implements LcsLength, LcsList {

    public <T> List<T> lcs(T[] a, T[] b) {
        Iterable<Integer> sequence = lcsItems(a, b);
        List<T> lcs = new ArrayList<>();
        for (int index : sequence) {
            lcs.add(a[index]);
        }
        return lcs;
    }

    @SuppressWarnings(value = "unchecked")
    protected Iterable<Integer> lcsItems(
            final List<?> a,
            final List<?> b) {
        final int n = a.size();
        final int m = b.size();
        return lcsItems(a.toArray(new Object[n]), b.toArray(new Object[m]));
    }

    protected abstract <T> Iterable<Integer> lcsItems(final T[] a, final T[] b);
}
