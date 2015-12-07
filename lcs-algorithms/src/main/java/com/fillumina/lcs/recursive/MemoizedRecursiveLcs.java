package com.fillumina.lcs.recursive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Improves the recursive algorithm by avoiding recurring on an already
 * calculated branch using
 * <a href='https://en.wikipedia.org/wiki/Memoization'>memoization</a>.
 *
 * This class is NOT thread safe!
 *
 * @see RecursiveLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MemoizedRecursiveLcs extends RecursiveLcs {
    private final Map<Long, List<?>> resultsMap = new HashMap<>();

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        resultsMap.clear();
        return super.lcs(a, b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, int n,
            List<? extends T> b, int m) {
        // packs two integer indexes in a single long for efficiency
        long l = (long)n << 32 | m & 0xFFFFFFFFL; // long64 = (int32,int32)
        @SuppressWarnings("unchecked")
        List<? extends T> result = (List<? extends T>) resultsMap.get(l);
        if (result != null) {
            return result;
        }
        result = super.lcs(a, n, b, m);
        resultsMap.put(l, result);
        return result;
    }
}
