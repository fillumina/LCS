package com.fillumina.lcs;

import java.util.Collection;
import java.util.List;

/**
 * General interface for LCS algorithm implementations.
 * <p>
 * Because the LCS algorithms heavily use random access to the sequences
 * the only fast container is the array. The collections will be transformed
 * in arrays using {@link Collection#toArray() } before the calculation.
 * In case of arrays of primitive (such as if you need to process Strings),
 * use a customized version of the abstract template classes.
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
