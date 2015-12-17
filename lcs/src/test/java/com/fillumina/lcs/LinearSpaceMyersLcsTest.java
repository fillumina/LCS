package com.fillumina.lcs;

import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsTest extends AbstractLcsLengthTest  {

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new LcsSizeEvaluatorAdaptor(new LinearSpaceMyersLcs());
    }
}