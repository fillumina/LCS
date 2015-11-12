package com.fillumina.lcs.recursive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Improves the recursive algorithm by avoiding recurring on an already
 * calculated branch using
 * <a href='https://en.wikipedia.org/wiki/Memoization'>memoization</a>.
 *
 * @see RecursiveLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MemoizedRecursiveLcs<T> extends RecursiveLcs<T> {
    private final Map<Long, List<T>> resultsMap = new HashMap<>();

    @Override
    public List<T> lcs(List<T> a, int n, List<T> b, int m) {
        // packs two integer indexes in a single long for efficiency
        long l = (long)n << 32 | m & 0xFFFFFFFFL; // long64 = (int32,int32)
        List<T> result = resultsMap.get(l);
        if (result != null) {
            return result;
        }
        result = super.lcs(a, n, b, m);
        resultsMap.put(l, result);
        return result;
    }
}
