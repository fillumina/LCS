package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.algorithm.scoretable.BottomUpLcs;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
public class BottomUpLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new BottomUpLcs();
    }
}
