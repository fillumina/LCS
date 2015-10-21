package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;
import static com.fillumina.lcs.util.ListUtils.*;
import java.util.Collections;

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
            return concatenate(lcs(xb, yb), xe);
        } else {
            return maxLenght(lcs(xs, yb), lcs(xb, ys));
        }
    }

    private static <T> List<T> allButLastElementSublist(List<T> list) {
        int size = list.size();
        if (size == 0) {
            return Collections.<T>emptyList();
        }
        return list.subList(0, size - 1);
    }

    private static <T> T getLastElement(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

}
