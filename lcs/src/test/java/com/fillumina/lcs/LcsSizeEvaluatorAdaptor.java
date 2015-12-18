package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.helper.LcsLength;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsSizeEvaluatorAdaptor implements LcsList, LcsLength {
    private final Lcs lcs;

    public LcsSizeEvaluatorAdaptor(final Lcs lcs) {
        this.lcs = lcs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<? extends T> lcs(
            List<? extends T> xs,
            List<? extends T> ys) {
        final DefaultLcsInput<T> lcsInput = new DefaultLcsInput<>(xs, ys);
        List<LcsItem> list = lcs.calculateLcs(lcsInput);
        return lcsInput.extractLcsList(list);
    }

    @Override
    public <T> int lcsLength(
            List<? extends T> xs,
            List<? extends T> ys) {
        final DefaultLcsInput<T> lcsInput = new DefaultLcsInput<>(xs, ys);
        return lcs.calculateLcsLength(lcsInput);
    }
}
