package com.fillumina.lcs;

import java.util.List;

/**
 * @see <a href="https://neil.fraser.name/writing/diff/">
 *  Neil Fraser: Diff Strategies
 * </a>
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CommonOptimizations<T> implements Lcs<T> {

    private final Lcs<T> delegate;

    public CommonOptimizations(Lcs<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {

        // 0) emptyness
        if (xs.isEmpty()) {
            return xs;
        }
        if (ys.isEmpty()) {
            return ys;
        }

        // 1) equality O(n)
        if (xs.equals(ys)) {
            return xs;
        }

        // 2) common prefix/suffix O(n)
        // because it's O(nm), reducing the elements to match can improve
        // the speed considerably and it only takes an O(n)
        int prefixSize = Math.min(xs.size(), ys.size());
        for (int i=0; i<prefixSize; i++) {
            //if (xs.get(i))
        }

        return delegate.lcs(xs, ys);
    }
}
