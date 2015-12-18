package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.myers.ReverseMyersLcs;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ReverseMyersLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new ReverseMyersLcs();
    }

}
