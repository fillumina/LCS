package com.fillumina.lcs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the Longest Common Subsequence algorithm.
 *
 */
public class MemoizedRecursiveLcs<T> extends RecursiveLcs<T> {
    private final Map<Operands<T>, List<T>> resultsMap = new HashMap<>();

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        Operands<T> o = new Operands<>(xs, ys);
        List<T> result = resultsMap.get(o);
        if (result != null) {
            return result;
        }
        result = super.lcs(xs, ys);
        resultsMap.put(o, result);
        return result;
    }

    private static class Operands<T> {
        private final List<T> xs, ys;
        private final int hashCode;

        public Operands(List<T> xs, List<T> ys) {
            this.xs = xs;
            this.ys = ys;
            this.hashCode = calculateHashCode();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        private int calculateHashCode() {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(xs);
            hash = 61 * hash + Objects.hashCode(ys);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Operands<?> other = (Operands<?>) obj;
            if (this.hashCode != other.hashCode) {
                return false;
            }
            if (!Objects.equals(this.xs, other.xs)) {
                return false;
            }
            if (!Objects.equals(this.ys, other.ys)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Operands{" + "hashCode=" + hashCode +
                    ", xs=" + xs + ", ys=" + ys + '}';
        }
    }
}
