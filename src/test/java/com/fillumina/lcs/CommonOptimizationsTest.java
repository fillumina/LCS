package com.fillumina.lcs;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.scoretable.BottomUpLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CommonOptimizationsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new CommonOptimizations<>(new BottomUpLcs<Character>());
    }
}
