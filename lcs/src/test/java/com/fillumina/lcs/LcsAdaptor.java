package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.helper.LcsList;
import java.util.List;

/**
 * The lcs module uses {@link Lcs} as a general interface
 * while the tests use {@link LcsList}. This is an adaptor that makes
 * the algorithms implementing {@link Lcs} testable with tests expecting
 * {@link LcsList}.
 *
 * @author Francesco Illuminati
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
