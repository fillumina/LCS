package com.fillumina.lcs.helper;

import java.util.List;

/**
 * Interface useful to test various LCS algorithms.
 *
 * @author Francesco Illuminati 
 */
public interface LcsList {

    <T> List<? extends T> lcs(List<? extends T> xs, List<? extends T> ys);
}
