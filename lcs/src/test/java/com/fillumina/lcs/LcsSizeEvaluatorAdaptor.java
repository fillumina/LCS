package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsSizeEvaluatorAdaptor implements LcsSizeEvaluator {
    private final Lcs lcs;
    private int size;

    public LcsSizeEvaluatorAdaptor(final Lcs lcs) {
        this.lcs = lcs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<? extends T> lcs(
            List<? extends T> xs,
            List<? extends T> ys) {
        CollectionLcsInput<? extends T> lcsInput =
                new CollectionLcsInput<>(xs, ys);
        LcsSequencer lcsSequencer = LcsItemSequencer.INSTANCE;
        List<LcsItem> result = lcs.calculateLcs(lcsInput, lcsSequencer);
        this.size = result.size();
        return lcsInput.extractLcsList(result);
    }

    @Override
    public int getLcsSize() {
        return size;
    }

}
