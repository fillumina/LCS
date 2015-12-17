package com.fillumina.lcs;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs<T> {

    List<LcsItem> lcIndexes(final Collection<? extends T> a,
            final Collection<? extends T> b);

    List<? extends T> lcSequence(final Collection<? extends T> a,
            final Collection<? extends T> b);

    List<LcsItem> lcIndexes(final T[] a, final T[] b);

    List<T> lcSequence(final T[] a, final T[] b);

    int lcsLength(final Collection<? extends T> a,
            final Collection<? extends T> b);

    int lcsLength(final T[] a, final T[] b);
}
