package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.ReverseMyersLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ReverseMyersLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new ReverseMyersLcs();
    }

}