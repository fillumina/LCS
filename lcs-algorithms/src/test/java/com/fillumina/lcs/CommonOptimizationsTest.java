package com.fillumina.lcs;

import com.fillumina.lcs.CommonOptimizations;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.scoretable.BottomUpLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CommonOptimizationsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new CommonOptimizations(new BottomUpLcs());
    }
}
