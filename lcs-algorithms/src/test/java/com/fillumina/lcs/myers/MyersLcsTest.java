package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.LcsList;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new MyersLcs();
    }
}
