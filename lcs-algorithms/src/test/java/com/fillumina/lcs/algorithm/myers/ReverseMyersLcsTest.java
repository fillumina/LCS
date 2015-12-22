package com.fillumina.lcs.algorithm.myers;

import com.fillumina.lcs.algorithm.myers.ReverseMyersLcs;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;

/**
 *
 * @author Francesco Illuminati
 */
public class ReverseMyersLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new ReverseMyersLcs();
    }

}
