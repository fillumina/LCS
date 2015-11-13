package com.fillumina.lcs;

import com.fillumina.lcs.LinearSpaceMyersLcs.LcsItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsAdaptor<T> implements Lcs<T> {

    @Override
    public List<T> lcs(final List<T> a, final List<T> b) {
        final LcsItem matches = lcsMatch(a, b);
        List<T> lcs = new ArrayList<>(matches.getSequenceSize());
        for (int index : matches.lcsIndexesOfTheFirstSequence()) {
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
        final LcsItem match = new LinearSpaceMyersLcsImpl<>(a, b).calculateLcs();
        return match == null ? LcsItem.NULL : match;
    }

    private static class LinearSpaceMyersLcsImpl<T> extends LinearSpaceMyersLcs {
        private final T[] a, b;

        public LinearSpaceMyersLcsImpl(T[] a, T[] b) {
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
