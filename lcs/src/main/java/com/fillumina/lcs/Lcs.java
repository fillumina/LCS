package com.fillumina.lcs;

import java.util.Collection;
import java.util.List;

/**
 * General interface for LCS algorithm implementations.
 *
 * @author Francesco Illuminati 
 */
public interface Lcs {

    <T> List<? extends T> calculateLcs(Collection<? extends T> a,
            Collection<? extends T> b);

    <T> List<LcsItem> calculateLcsIndexes(Collection<? extends T> a,
            Collection<? extends T> b);

    <T> int calculateLcsLength(Collection<? extends T> a,
            Collection<? extends T> b);

    <T> List<? extends T> calculateLcs(Object[] a, Object[] b);

    List<LcsItem> calculateLcsIndexes(Object[] a, Object[] b);

    int calculateLcsLength(Object[] a, Object[] b);
}
