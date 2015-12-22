package com.fillumina.lcs.algorithm;

import com.fillumina.lcs.algorithm.scoretable.BottomUpLcs;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;

/**
 *
 * @author Francesco Illuminati
 */
public class CommonOptimizationsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new CommonOptimizations(new BottomUpLcs());
    }
}
