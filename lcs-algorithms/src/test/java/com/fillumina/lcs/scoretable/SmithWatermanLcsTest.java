package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.scoretable.SmithWatermanLcs;

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
