package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ObservableRecursiveLcs<T> extends RecursiveLcs<T>
        implements ObservableLcs<T> {

    private LcsCallListener<T> listener;

    @Override
    public void setListener(LcsCallListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        listener.notifyLcs(xs, ys);
        return super.lcs(xs, ys);
    }
}
