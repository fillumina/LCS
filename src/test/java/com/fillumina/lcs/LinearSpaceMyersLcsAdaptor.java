package com.fillumina.lcs;

import java.util.List;

/**
 * Adaptor that makes {@link AbstractLinearSpaceMyersLcs} callable using
 * the {@link Lcs} interface.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsAdaptor implements LcsSizeEvaluator {
    private LinearSpaceMyersLcs linearSpaceMyersLcs;

    @Override
    public int getLcs() {
        return linearSpaceMyersLcs.getLcsLength();
    }

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        linearSpaceMyersLcs = LinearSpaceMyersLcs.lcs(a, b);
        linearSpaceMyersLcs.calculateLcs();
        return linearSpaceMyersLcs.calcaulateLcsList();
    }
}
