package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs {
    
    <T> List<? extends T> calculateLcs(List<? extends T> a, List<? extends T> b);

    <T> List<LcsItem> calculateLcsIndexes(List<? extends T> a, List<? extends T> b);

    <T> int calculateLcsLength(List<? extends T> a, List<? extends T> b);
}
