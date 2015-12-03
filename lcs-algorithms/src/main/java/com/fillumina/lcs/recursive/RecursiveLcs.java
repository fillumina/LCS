package com.fillumina.lcs.recursive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.Lcs;

/**
 * The simplest LCS algorithm. It is heavily based on recursion so it's quite
 * impractical but offers a very concise and clear implementation.
 *
 * @see MomoizedRecursiveLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RecursiveLcs implements Lcs {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        return lcs(a, a.size(), b, b.size());
    }

    public <T> List<? extends T> lcs(List<? extends T> a, int n,
            List<? extends T> b, int m) {
        if (n == 0 || m == 0) {
            return Collections.<T>emptyList();
        }

        T xe = a.get(n-1);
        T ye = b.get(m-1);

        if (Objects.equals(xe, ye)) {
            return concatenate(lcs(a, n-1, b, m-1), xe);
        } else {
            return longest(lcs(a, n, b, m-1), lcs(a, n-1, b, m));
        }
    }

    static <T> List<? extends T> concatenate(List<? extends T> result, T xe) {
        List<T> list = new ArrayList<>(result.size() + 1);
        list.addAll(result);
        list.add(xe);
        return list;
    }

    static <T> List<? extends T> longest(List<? extends T> a, List<? extends T> b) {
        return (a.size() > b.size() ? a : b);
    }

}
