package com.fillumina.lcs;

import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface ObservableLcs<T> extends Lcs<T> {

    void setListener(LcsCallListener<T> listener);
}
