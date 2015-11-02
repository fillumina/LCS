package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Longest Common Subsequence algorithm.
 * It's heavily using recursion so it's VERY memory hungry and time consuming.
 *
 */
public class RecursiveLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        return lcs(xs, xs.size(), ys, ys.size());
    }

    public List<T> lcs(List<T> xs, int n, List<T> ys, int m) {
        if (n == 0 || m == 0) {
            return new ArrayList<>();
        }

        T xe = xs.get(n-1);
        T ye = ys.get(m-1);

        if (xe.equals(ye)) {
            List<T> result = lcs(xs, n-1, ys, m-1);
            result.add(xe);
            return result;
        } else {
            return maxLenght(lcs(xs, n, ys, m-1), lcs(xs, n-1, ys, m));
        }
    }

    public static <T> List<T> maxLenght(List<T> a, List<T> b) {
        return (a.size() > b.size() ? a : b);
    }
}
