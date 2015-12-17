package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsSizeEvaluatorAdaptor implements LcsSizeEvaluator {
    private final Lcs<?> lcs;
    private int size;

    public LcsSizeEvaluatorAdaptor(final Lcs<?> lcs) {
        this.lcs = lcs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<? extends T> lcs(
            List<? extends T> xs,
            List<? extends T> ys) {
        final List<? extends T> list = ((Lcs<T>)lcs).lcSequence(xs, ys);
        this.size = list.size();
        return list;
    }

    @Override
    public int getLcsSize() {
        return size;
    }

}
