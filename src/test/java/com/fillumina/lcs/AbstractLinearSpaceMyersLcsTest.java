package com.fillumina.lcs;

import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class AbstractLinearSpaceMyersLcsTest extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new AbstractLinearSpaceMyersLcsTest().randomLcs(600, 500, 1);
    }

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new LinearSpaceMyersLcsAdaptor();
    }
}
