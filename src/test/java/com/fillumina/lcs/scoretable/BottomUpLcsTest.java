package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BottomUpLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new BottomUpLcs<>();
    }
}
