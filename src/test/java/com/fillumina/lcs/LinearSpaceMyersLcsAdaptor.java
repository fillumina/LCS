package com.fillumina.lcs;

/**
 * Adaptor that makes {@link AbstractLinearSpaceMyersLcs} callable using
 * the {@link Lcs} interface.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsAdaptor extends LinearSpaceMyersLcs
        implements LcsSizeEvaluator {

    private LcsItem sequence;

    @Override
    public int getLcs() {
        return sequence.getSequenceSize();
    }

    @Override
    protected <T> LcsItem lcsSequence(T[] a, T[] b) {
        this.sequence = super.lcsSequence(a, b);
        return sequence;
    }
}
