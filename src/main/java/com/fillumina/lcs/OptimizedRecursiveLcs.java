package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;
import static com.fillumina.lcs.util.ListUtils.*;

/**
 * 
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedRecursiveLcs<T> implements ListLcs<T> {

    @Override
    public List<T> lcs(List<T> xs, List<T> ys) {
        return optimizedLcs(xs, ys, xs.size(), ys.size());
    }

    private List<T> optimizedLcs(List<T> xs, List<T> ys, int i, int j) {
        if (i == 0 || j == 0) {
            return new ArrayList<>();
        }

        T xe = xs.get(i - 1);
        T ye = ys.get(j - 1);

        if (xe.equals(ye)) {
            return concatenate(optimizedLcs(xs, ys, i - 1, j - 1), xe);
        } else {
            return maxLenght(
                    optimizedLcs(xs, ys, i, j - 1),
                    optimizedLcs(xs, ys, i - 1, j));
        }

    }
}
