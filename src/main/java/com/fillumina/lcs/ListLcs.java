package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface ListLcs<T> {

    List<T> lcs(List<T> xs, List<T> ys);
}
