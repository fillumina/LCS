package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface LcsCallListener<T> {

    void notifyLcs(List<T> xs, List<T> ys);
}
