package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs<T> {

    List<T> lcs(List<T> xs, List<T> ys);
}
