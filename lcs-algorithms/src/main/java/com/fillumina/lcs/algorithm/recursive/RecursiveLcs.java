package com.fillumina.lcs.algorithm.recursive;

import com.fillumina.lcs.helper.LcsList;
import java.util.List;
import java.util.Objects;

/**
 * The simplest LCS algorithm. It is based on recursion so it's quite
 * impractical (it's time is exponential) but it offers a very concise and
 * clear implementation.
 *
 * @see MomoizedRecursiveLcs
 * @author Francesco Illuminati
 */
public class RecursiveLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        return recursiveLcs(a, a.length, b, b.length).toList();
    }

    @SuppressWarnings("unchecked")
    <T> Stack<T> recursiveLcs(T[] a, int n, T[] b, int m) {
        if (n == 0 || m == 0) {
            return Stack.<T>emptyStack();
        }

        T x = a[n-1];
        T y = b[m-1];

        if (Objects.equals(x, y)) {
            return recursiveLcs(a, n-1, b, m-1).push(x);
        } else {
            return longest(recursiveLcs(a, n, b, m-1), recursiveLcs(a, n-1, b, m));
        }
    }

    static <T> Stack<T> longest(Stack<T> a, Stack<T> b) {
        return (a.size() > b.size() ? a : b);
    }
}
