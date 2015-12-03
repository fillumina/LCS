package com.fillumina.lcs.myers;

import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.testutil.AbstractLcsTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new MyersLcs();
    }
}