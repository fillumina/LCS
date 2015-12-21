package com.fillumina.lcs;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs {

    <T> List<? extends T> calculateLcs(Collection<? extends T> a,
            Collection<? extends T> b);

    <T> List<LcsItem> calculateLcsIndexes(Collection<? extends T> a,
            Collection<? extends T> b);

    <T> int calculateLcsLength(Collection<? extends T> a,
            Collection<? extends T> b);
}
