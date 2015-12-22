package com.fillumina.lcs.algorithm.hirschberg;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import java.util.List;

/**
 *
 * @author Francesco Illuminati
 */
public class OptimizedHirschbergLinearSpaceLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new OptimizedHirschbergLinearSpaceLcs() {

            @Override
            public <T> List<T> lcs(T[] xs, T[] ys) {
                count(xs, ys); // counts how many times lcs() is called
                return super.lcs(xs, ys);
            }
        };
    }
}
