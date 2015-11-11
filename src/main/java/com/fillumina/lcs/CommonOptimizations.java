package com.fillumina.lcs;

import static com.fillumina.lcs.util.ListUtils.concatenate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.fillumina.lcs.util.ListUtils.concatenate;

/**
 * @see <a href="https://neil.fraser.name/writing/diff/">
 *  Neil Fraser: Diff Strategies
 * </a>
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CommonOptimizations<T> implements ListLcs<T> {

    private final ListLcs<T> delegate;

    public CommonOptimizations(ListLcs<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {

        // 0) emptyness
        if (xs.isEmpty()) {
            return Collections.<T>emptyList();
        }
        if (ys.isEmpty()) {
            return Collections.<T>emptyList();
        }

        // 1) equality O(n)
        if (xs.equals(ys)) {
            return new ArrayList<>(xs);
        }

        // one column
        final int n = xs.size();
        final int m = ys.size();

        if (n == 1) {
            T t = xs.get(0);
            for (int i=0; i<m; i++) {
                if (t.equals(ys.get(i))) {
                    return Collections.singletonList(t);
                }
            }
        }

        // one row
        if (m == 1) {
            T t = ys.get(0);
            for (int i=0; i<n; i++) {
                if (t.equals(xs.get(i))) {
                    return Collections.singletonList(t);
                }
            }
        }

        // 2) common prefix/suffix O(n)
        // because it's O(nm), reducing the elements to match can improve
        // the speed considerably and it only takes an O(n)
        final int min = Math.min(n, m);
        int d,u;
        for (d=0; d<min && xs.get(d).equals(ys.get(d)); d++);
        final List<T> before = (d > 0) ? xs.subList(0, d) : null;
        if (d == min) {
            return before;
        }
        for (u=0; u<min && xs.get(n-u-1).equals(ys.get(m-u-1)); u++);
        if (d == 0 && u == 0) {
            return delegate.lcs(xs, ys);
        }
        final List<T> middle = delegate.lcs(xs.subList(d,n-u), ys.subList(d,m-u));
        final List<T> after = (u > 0) ? xs.subList(n-u,n) : null;
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
