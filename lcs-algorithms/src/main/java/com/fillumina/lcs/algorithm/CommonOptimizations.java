package com.fillumina.lcs.algorithm;

import com.fillumina.lcs.helper.LcsList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Some general optimizations that can improve considerably
 * the speed of any LCS algorithm by reducing the length of the
 * sequences it has to work with.
 * <ul>
 * <li>check if the input sequences are empty O(1);
 * <li>check if they are equals O(n);
 * <li>check if they are just one row/column O(n);
 * <li>check for equal heads and tails;
 * </ul>
 *
 * @see <a href="https://neil.fraser.name/writing/diff/">
 *  Neil Fraser: Diff Strategies
 * </a>
 * @author Francesco Illuminati
 */
public class CommonOptimizations implements LcsList {

    private final LcsList delegate;

    public CommonOptimizations(LcsList delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        final int n = a.length;
        final int m = b.length;

        // emptyness
        if (n == 0) {
            return Collections.<T>emptyList();
        }
        if (m == 0) {
            return Collections.<T>emptyList();
        }

        // equality
        if (Arrays.equals(a, b)) {
            return Arrays.asList(a);
        }

        // one column
        if (n == 1) {
            T t = a[0];
            for (int i=0; i<m; i++) {
                if (Objects.equals(t, b[i])) {
                    return Collections.singletonList(t);
                }
            }
        }

        // one row
        if (m == 1) {
            T t = b[0];
            for (int i=0; i<n; i++) {
                if (Objects.equals(t, a[i])) {
                    return Collections.singletonList(t);
                }
            }
        }

        // common prefix/suffix O(n)
        final int min = n < m ? n : m;
        int d;
        for (d=0; d<min && Objects.equals(a[d], b[d]); d++) {}
        final List<T> before = (d > 0) ? Arrays.asList(a).subList(0, d) : null;
        if (d == min) {
            return before;
        }
        int u;
        for (u=0; u<(min-d) && Objects.equals(a[n-u-1], b[m-u-1]); u++) {}
        if (d == 0 && u == 0) {
            return delegate.lcs(a, b);
        }

        final List<T> middle = delegate.lcs(Arrays.copyOfRange(a, d, n-u),
                        Arrays.copyOfRange(b, d, m-u));

        final List<T> after = (u > 0) ? Arrays.asList(a).subList(n-u,n) : null;
        
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

    /** The given lists are not modified. */
    static <T> List<T> concatenate(List<T> a, List<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        List<T> l = new ArrayList<>(a.size() + b.size());
        l.addAll(a);
        l.addAll(b);
        return Collections.unmodifiableList(l);
    }
}
