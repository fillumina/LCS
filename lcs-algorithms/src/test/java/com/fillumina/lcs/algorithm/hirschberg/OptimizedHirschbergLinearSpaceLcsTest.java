package com.fillumina.lcs.algorithm.hirschberg;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;

/**
 *
 * @author Francesco Illuminati
 */
public class OptimizedHirschbergLinearSpaceLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new OptimizedHirschbergLinearSpaceLcs();
    }
}
