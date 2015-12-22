package com.fillumina.lcs.helper;

import java.util.List;

/**
 * Interface useful to test various LCS algorithms.
 *
 * @author Francesco Illuminati
 */
public interface LcsList {

    <T> List<T> lcs(T[] xs, T[] ys);
}
