package com.fillumina.lcs;

import java.util.List;

/**
 * Interface useful to test various LCS algorithms.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface ListLcs {

    <T> List<? extends T> lcs(List<? extends T> xs, List<? extends T> ys);
}
