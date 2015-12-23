package com.fillumina.lcs.algorithm.performance;

import com.fillumina.lcs.Lcs;
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
public class LcsLengthAdaptor implements LcsList, LcsLength {
    private final Lcs lcs;

    public LcsLengthAdaptor(final Lcs lcs) {
        this.lcs = lcs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> lcs(T[] xs, T[] ys) {
        return lcs.calculateLcs(xs, ys);
    }

    @Override
    public int lcsLength(Object[] xs, Object[] ys) {
        return lcs.calculateLcsLength(xs, ys);
    }
}
