package com.fillumina.lcs.algorithm;

import com.fillumina.lcs.algorithm.CommonOptimizations;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.algorithm.scoretable.BottomUpLcs;

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
