package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsSizeEvaluatorAdaptor;
import java.util.Objects;
import com.fillumina.lcs.myers.AbstractParallelLinearSpaceMyersLcs.LcsItem;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class AbstractParallelLinearSpaceMyersLcsAdaptor
        extends AbstractLcsSizeEvaluatorAdaptor {
    private LcsItem lcsItem;

    @Override
    protected <T> Iterable<Integer> lcsItems(T[] a, T[] b) {
        lcsItem = new ParallelLinearSpaceMyersLcsImpl<>(a, b).calculateLcs();
        return lcsItem.lcsIndexes();
    }

    @Override
    public int getLcs() {
        return lcsItem.getLcs();
    }

    private static class ParallelLinearSpaceMyersLcsImpl<T>
            extends AbstractParallelLinearSpaceMyersLcs {
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
