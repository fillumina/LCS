package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.ListLcs;
import com.fillumina.lcs.scoretable.SmithWatermanLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SmithWatermanLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new SmithWatermanLcs<>();
    }
}
