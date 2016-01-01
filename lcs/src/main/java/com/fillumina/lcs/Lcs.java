package com.fillumina.lcs;

import java.util.List;

/**
 * General interface for LCS algorithm implementations.
 *
 * @author Francesco Illuminati
 */
public interface Lcs {

    <T> List<T> calculateLcs(T[] a, T[] b);

    List<LcsItem> calculateLcsIndexes(Object[] a, Object[] b);

    int calculateLcsLength(Object[] a, Object[] b);
}
