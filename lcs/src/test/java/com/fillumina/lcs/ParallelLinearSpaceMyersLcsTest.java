package com.fillumina.lcs;

import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ParallelLinearSpaceMyersLcsTest
        extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new ParallelLinearSpaceMyersLcsTest().randomLcs(60, 10, 100);
    }

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new LcsSizeEvaluatorAdaptor(ParallelLinearSpaceMyersLcs.INSTANCE);
    }
}
