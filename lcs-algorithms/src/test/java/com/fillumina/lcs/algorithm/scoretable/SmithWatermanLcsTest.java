package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;

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
