package com.fillumina.lcs.algorithm.myers;

import com.fillumina.lcs.algorithm.myers.MyersLcs;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
public class MyersLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new MyersLcs();
    }
}
