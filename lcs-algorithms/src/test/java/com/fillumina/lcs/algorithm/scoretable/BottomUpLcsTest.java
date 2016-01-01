package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.algorithm.scoretable.graphical.BottomUpLcs;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;

/**
 *
 * @author Francesco Illuminati
 */
public class BottomUpLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new BottomUpLcs();
    }
}
