package com.fillumina.lcs.myers;

import com.fillumina.lcs.LcsSizeEvaluator;
import com.fillumina.lcs.myers.AbstractParallelLinearSpaceMyersLcsAdaptor;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class AbstractParallelLinearSpaceMyersLcsTest
        extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new AbstractParallelLinearSpaceMyersLcsTest().randomLcs(60, 10, 100);
    }

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new AbstractParallelLinearSpaceMyersLcsAdaptor();
    }
}
