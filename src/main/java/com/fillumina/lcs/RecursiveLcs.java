package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The most simple LCS algorithm. It is heavily based on recursion so it's quite
 * impractical but offers a very concise and clear implementation.
 *
 * @see MomoizedRecursiveLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RecursiveLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        return lcs(xs, xs.size(), ys, ys.size());
    }

    public List<T> lcs(List<T> xs, int n, List<T> ys, int m) {
        if (n == 0 || m == 0) {
            return Collections.<T>emptyList();
        }

        T xe = xs.get(n-1);
        T ye = ys.get(m-1);

        if (xe.equals(ye)) {
            return concatenate(lcs(xs, n-1, ys, m-1), xe);
        } else {
            return longest(lcs(xs, n, ys, m-1), lcs(xs, n-1, ys, m));
        }
    }

    static <T> List<T> concatenate(List<T> result, T xe) {
        List<T> list = new ArrayList<>(result.size() + 1);
        list.addAll(result);
        list.add(xe);
        return list;
    }

    static <T> List<T> longest(List<T> a, List<T> b) {
        return (a.size() > b.size() ? a : b);
    }

}
