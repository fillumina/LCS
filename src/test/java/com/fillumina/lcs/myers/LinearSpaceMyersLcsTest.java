package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new LinearSpaceMyersLcs<>();
    }
}
