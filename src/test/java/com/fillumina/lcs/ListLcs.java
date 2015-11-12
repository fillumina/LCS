package com.fillumina.lcs;

import java.util.List;

/**
 * It's an easy interface for LCS algorithms. For a more flexible one
 * see {@link com.fillumina.lcs.myers.Lcs}.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface ListLcs<T> {

    List<T> lcs(List<T> xs, List<T> ys);
}
