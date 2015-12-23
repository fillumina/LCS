package com.fillumina.lcs.algorithm.recursive;

import com.fillumina.lcs.helper.LcsList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Improves the recursive algorithm by avoiding recurring on an already
 * calculated branch using
 * <a href='https://en.wikipedia.org/wiki/Memoization'>memoization</a>.
 *
 * @see RecursiveLcs
 * @author Francesco Illuminati
 */
public class MemoizedRecursiveLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        return new Inner().lcs(a, b);
    }

    static class Inner extends RecursiveLcs {
        private final Map<Long, Stack<?>> resultsMap = new HashMap<>();

        @Override
        public <T> List<T> lcs(T[] a, T[] b) {
            return super.lcs(a, b);
        }

        @Override
        @SuppressWarnings("unchecked")
        <T> Stack<T> recursiveLcs(T[] a, int n, T[] b, int m) {
            // packs two integer indexes in a single long for efficiency
            long key = (long)n << 32 | m & 0xFFFFFFFFL; // long64 = (int32,int32)
            @SuppressWarnings("unchecked")
            Stack<?> result = resultsMap.get(key);
            if (result == null) {
                result = super.recursiveLcs(a, n, b, m);
                resultsMap.put(key, result);
            }
            return (Stack<T>) result;
        }
    }
}
