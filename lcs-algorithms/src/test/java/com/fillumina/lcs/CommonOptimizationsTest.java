package com.fillumina.lcs;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.scoretable.BottomUpLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CommonOptimizationsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new CommonOptimizations(new BottomUpLcs());
    }
}
