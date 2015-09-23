package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Longest Common Subsequence algorithm.
 *
 */
public class RecursiveLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        if (xs.isEmpty() || ys.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> xb = allButLastElementSublist(xs);
        T xe = getLastElement(xs);
        List<T> yb = allButLastElementSublist(ys);
        T ye = getLastElement(ys);

        if (xe.equals(ye)) {
            return add(lcs(xb, yb), xe);
        } else {
            return maxLenght(lcs(xs, yb), lcs(xb, ys));
        }
    }

    private List<T> allButLastElementSublist(List<T> list) {
        int size = list.size();
        if (size == 0) {
            return Collections.<T>emptyList();
        }
        return list.subList(0, size - 1);
    }

    private T getLastElement(List<T> list) {
        return list.get(list.size() - 1);
    }

    private List<T> add(List<T> list, T element) {
        list.add(element);
        return list;
    }

    private List<T> maxLenght(List<T> a, List<T> b) {
        return a.size() > b.size() ? a : b;
    }
}
