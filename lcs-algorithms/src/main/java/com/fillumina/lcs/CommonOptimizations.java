package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.fillumina.lcs.util.ListUtils.concatenate;
import java.util.Objects;

/**
 * Some general optimizations that can improve considerably
 * the speed of any LCS algorithm by reducing the length of the
 * sequences it has to work with.
 * <ul>
 * <li>check if the input sequences are empty O(1);
 * <li>check if they are equals O(n);
 * <li>check if they are just one row/column O(1) + O(n);
 * <li>check for equal heads and/or tails;
 * </ul>
 *
 * @see <a href="https://neil.fraser.name/writing/diff/">
 *  Neil Fraser: Diff Strategies
 * </a>
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CommonOptimizations implements Lcs {

    private final Lcs delegate;

    public CommonOptimizations(Lcs delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        final int n = a.size();
        final int m = b.size();

        // emptyness
        if (n == 0) {
            return Collections.<T>emptyList();
        }
        if (m == 0) {
            return Collections.<T>emptyList();
        }

        // equality
        if (a.equals(b)) {
            return new ArrayList<>(a);
        }

        // one column
        if (n == 1) {
            T t = a.get(0);
            for (int i=0; i<m; i++) {
                if (Objects.equals(t, b.get(i))) {
                    return Collections.singletonList(t);
                }
            }
        }

        // one row
        if (m == 1) {
            T t = b.get(0);
            for (int i=0; i<n; i++) {
                if (Objects.equals(t, a.get(i))) {
                    return Collections.singletonList(t);
                }
            }
        }

        // common prefix/suffix O(n)
        final int min = n < m ? n : m;
        int d;
        for (d=0; d<min && Objects.equals(a.get(d), b.get(d)); d++) {}
        final List<? extends T> before = (d > 0) ? a.subList(0, d) : null;
        if (d == min) {
            return before;
        }
        int u;
        for (u=0; u<(min-d) && Objects.equals(a.get(n-u-1), b.get(m-u-1)); u++) {}
        if (d == 0 && u == 0) {
            return delegate.lcs(a, b);
        }

        final List<? extends T> middle =
                delegate.lcs(a.subList(d,n-u), b.subList(d,m-u));

        final List<? extends T> after = (u > 0) ? a.subList(n-u,n) : null;
        if (d > 0) {
            if (!middle.isEmpty()) {
                if (u > 0) {
                    return concatenate(before, concatenate(middle, after));
                }
                return concatenate(before, middle);
            } else {
                if (u > 0) {
                    return concatenate(before, after);
                }
                return before;
            }
        } else {
            if (!middle.isEmpty()) {
                if (u > 0) {
                    return concatenate(middle, after);
                }
                return before;
            } else {
                if (u > 0) {
                    return after;
                }
                return Collections.<T>emptyList();
            }
        }
    }
}
