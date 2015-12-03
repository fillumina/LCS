package com.fillumina.lcs.myers;

import com.fillumina.lcs.LinearSpaceMyersLcsAdaptor;
import com.fillumina.lcs.LcsSizeEvaluator;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BaselineOptimizedLinearSpaceMyersLcsTest
        extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new BaselineOptimizedLinearSpaceMyersLcsTest().randomLcs(60, 10, 100);
    }

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new LinearSpaceMyersLcsAdaptor();
    }
}
