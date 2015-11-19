package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An helper that allows to pass to the LCS algorithm lists or arrays.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs {

    public <T> List<T> lcs(
            final List<? extends T> a,
            final List<? extends T> b) {
        LcsItem lcsItem = lcsSequence(a, b);
        List<T> lcs = new ArrayList<>(lcsItem.getSequenceSize());
        for (int index : lcsItem.lcsIndexesOfTheFirstSequence()) {
            lcs.add(a.get(index));
        }
        return lcs;
    }

    @SuppressWarnings(value = "unchecked")
    public LcsItem lcsSequence(final List<?> a, final List<?> b) {
        final int n = a.size();
        final int m = b.size();
        return lcsSequence(a.toArray(new Object[n]), b.toArray(new Object[m]));
    }

    protected <T> LcsItem lcsSequence(T[] a, T[] b) {
        return new LinearSpaceMyersLcsImpl<>(a,b).calculateLcs();
    }

    private static class LinearSpaceMyersLcsImpl<T>
            extends AbstractLinearSpaceMyersLcs {
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
