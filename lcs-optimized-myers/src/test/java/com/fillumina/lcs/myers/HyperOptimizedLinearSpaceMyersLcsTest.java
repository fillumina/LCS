package com.fillumina.lcs.myers;

import com.fillumina.lcs.LcsSizeEvaluator;
import com.fillumina.lcs.myers.HyperOptimizedLinearSpaceMyersLcsAdaptor;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class HyperOptimizedLinearSpaceMyersLcsTest
        extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new HyperOptimizedLinearSpaceMyersLcsTest().randomLcs(60, 10, 100);
    }

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new HyperOptimizedLinearSpaceMyersLcsAdaptor();
    }
}
