package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SmithWatermanLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new SmithWatermanLcs();
    }
}
