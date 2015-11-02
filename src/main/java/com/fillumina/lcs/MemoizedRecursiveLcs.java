package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the Longest Common Subsequence algorithm.
 *
 */
public class MemoizedRecursiveLcs<T> extends RecursiveLcs<T> {
    private final Map<Long, List<T>> resultsMap = new HashMap<>();

    @Override
    public List<T> lcs(List<T> xs, int n, List<T> ys, int m) {
        long l = (long)n << 32 | m & 0xFFFFFFFFL; // long64 = (int32,int32)
        List<T> result = resultsMap.get(l);
        if (result != null) {
            return result;
        }
        result = super.lcs(xs, n, ys, m);
        resultsMap.put(l, result);
        return result;
    }
}
