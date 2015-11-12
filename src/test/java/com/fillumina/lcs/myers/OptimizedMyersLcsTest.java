package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedMyersLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new OptimizedMyersLcs<>();
    }
}
