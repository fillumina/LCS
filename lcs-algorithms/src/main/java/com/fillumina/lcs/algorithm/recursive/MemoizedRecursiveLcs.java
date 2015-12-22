package com.fillumina.lcs.algorithm.recursive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Improves the recursive algorithm by avoiding recurring on an already
 * calculated branch using
 * <a href='https://en.wikipedia.org/wiki/Memoization'>memoization</a>.
 * <p>
 * This class is NOT thread safe!
 *
 * @see RecursiveLcs
 * @author Francesco Illuminati 
 */
public class MemoizedRecursiveLcs extends RecursiveLcs {
    private final Map<Long, Stack<?>> resultsMap = new HashMap<>();

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        resultsMap.clear();
        return super.lcs(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    <T> Stack<T> recursiveLcs(List<? extends T> a, int n,
            List<? extends T> b, int m) {
        // packs two integer indexes in a single long for efficiency
        long l = (long)n << 32 | m & 0xFFFFFFFFL; // long64 = (int32,int32)
        @SuppressWarnings("unchecked")
        Stack<?> result = resultsMap.get(l);
        if (result == null) {
            result = super.recursiveLcs(a, n, b, m);
            resultsMap.put(l, result);
        }
        return (Stack<T>) result;
    }
}
