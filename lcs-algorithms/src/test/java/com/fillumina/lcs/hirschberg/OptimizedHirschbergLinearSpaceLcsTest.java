package com.fillumina.lcs.hirschberg;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import java.util.List;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
public class OptimizedHirschbergLinearSpaceLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new OptimizedHirschbergLinearSpaceLcs() {

            @Override
            public <T> List<? extends T> lcs(List<? extends T> xs,
                    List<? extends T> ys) {
                count(xs, ys); // counts how many times lcs() is called
                return super.lcs(xs, ys);
            }
        };
    }
}
