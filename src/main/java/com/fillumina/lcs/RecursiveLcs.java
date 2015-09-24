package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;
import static com.fillumina.lcs.ListUtils.*;

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
}
