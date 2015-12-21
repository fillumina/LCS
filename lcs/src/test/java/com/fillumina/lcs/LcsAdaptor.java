package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.helper.LcsLength;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsAdaptor implements LcsList, LcsLength {
    private final Lcs lcs;

    public LcsAdaptor(final Lcs lcs) {
        this.lcs = lcs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<? extends T> lcs(
            List<? extends T> xs,
            List<? extends T> ys) {
        return lcs.calculateLcs(xs, ys);
    }

    @Override
    public <T> int lcsLength(
            List<? extends T> xs,
            List<? extends T> ys) {
        return lcs.calculateLcsLength(xs, ys);
    }
}
