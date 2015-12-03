package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.OptimizedMyersLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedMyersLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new OptimizedMyersLcs();
    }
}
