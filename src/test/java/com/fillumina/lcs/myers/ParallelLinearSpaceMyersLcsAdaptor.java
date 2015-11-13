package com.fillumina.lcs.myers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fillumina.lcs.myers.ParallelLinearSpaceMyersLcs.LcsItem;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ParallelLinearSpaceMyersLcsAdaptor<T> implements Lcs<T> {

    @Override
    public List<T> lcs(final List<T> a, final List<T> b) {
        final LcsItem matches = lcsMatch(a, b);
        List<T> lcs = new ArrayList<>(matches.getLcs());
        for (int index : matches.lcsIndexes()) {
            lcs.add(a.get(index));
        }
        return lcs;
    }

    @SuppressWarnings("unchecked")
    public LcsItem lcsMatch(final List<T> a, final List<T> b) {
        final int n = a.size();
        final int m = b.size();
        return lcsMatch((T[]) a.toArray(new Object[n]),
                (T[]) b.toArray(new Object[m]));
    }

    public LcsItem lcsMatch(final T[] a, final T[] b) {
        final LcsItem match =
                new ParallelLinearSpaceMyersLcsImpl<>(a, b).calculateLcs();
        return match == null ? LcsItem.NULL : match;
    }

    private static class ParallelLinearSpaceMyersLcsImpl<T>
            extends ParallelLinearSpaceMyersLcs {
        private final T[] a, b;

        public ParallelLinearSpaceMyersLcsImpl(T[] a, T[] b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(int x, int y) {
            return Objects.equals(a[x], b[y]);
        }

        @Override
        public int getFirstSequenceLength() {
            return a == null ? 0 : a.length;
        }

        @Override
        public int getSecondSequenceLength() {
            return b == null ? 0 : b.length;
        }
    }
}
