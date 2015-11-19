package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLcsSizeEvaluatorAdaptor implements LcsSizeEvaluator {

    @Override
    public <T> List<T> lcs(
            final List<? extends T> a,
            final List<? extends T> b) {
        Iterable<Integer> sequence = lcsItems(a, b);
        List<T> lcs = new ArrayList<>(getLcs());
        for (int index : sequence) {
            lcs.add(a.get(index));
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
