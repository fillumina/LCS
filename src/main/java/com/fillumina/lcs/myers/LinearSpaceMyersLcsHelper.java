package com.fillumina.lcs.myers;

import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.LinearSpaceMyersLcs.Match;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The indexes are passed along the calls so to avoid using sublists.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsHelper<T> implements Lcs<T> {

    @Override
    public List<T> lcs(final List<T> a, final List<T> b) {
        final Match matches = lcsMatch(a, b);
        List<T> lcs = new ArrayList<>(matches.getLcs());
        for (int index : matches.lcsIndexes()) {
            lcs.add(a.get(index));
        }
        return lcs;
    }

    @SuppressWarnings("unchecked")
    public Match lcsMatch(final List<T> a, final List<T> b) {
        final int n = a.size();
        final int m = b.size();
        return lcsMatch((T[]) a.toArray(new Object[n]),
                (T[]) b.toArray(new Object[m]));
    }

    public Match lcsMatch(final T[] a, final T[] b) {
        final Match match = new InnerLcs<>(a, b).getMatch();
        return match == null ? Match.NULL : match;
    }

    private static class InnerLcs<T> extends LinearSpaceMyersLcs<T> {
        private final T[] a, b;

        public InnerLcs(T[] a, T[] b) {
            this.a = a;
            this.b = b;
        }

        @Override
        boolean equals(int x, int y) {
            return Objects.equals(a[x], b[y]);
        }

        @Override
        int getLengthA() {
            return a == null ? 0 : a.length;
        }

        @Override
        int getLengthB() {
            return b == null ? 0 : b.length;
        }
    }
}
