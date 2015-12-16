package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsSizeEvaluatorAdaptor;
import com.fillumina.lcs.*;
import com.fillumina.lcs.myers.HyperOptimizedLinearSpaceMyersLcs.LcsItem;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class HyperOptimizedLinearSpaceMyersLcsAdaptor
        extends AbstractLcsSizeEvaluatorAdaptor {
    private LcsItem lcsItem;

    public <T> Iterable<Integer> lcsItems(final T[] a, final T[] b) {
        lcsItem = new LinearSpaceMyersLcsImpl<>(a, b).calculateLcs();
        return lcsItem.lcsIndexesOfTheFirstSequence();
    }

    @Override
    public int getLcsSize() {
        return lcsItem.getSequenceSize();
    }

    private static class LinearSpaceMyersLcsImpl<T>
            extends HyperOptimizedLinearSpaceMyersLcs {
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
