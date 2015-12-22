package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.algorithm.scoretable.SmithWatermanLcs;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
public class SmithWatermanLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new SmithWatermanLcs();
    }
}
