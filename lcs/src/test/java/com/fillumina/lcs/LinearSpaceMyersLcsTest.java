package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsTest extends AbstractLcsLengthTest  {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends LcsList & LcsLength> T getLcsSequenceGenerator() {
        return (T) new LcsSizeEvaluatorAdaptor(LinearSpaceMyersLcs.INSTANCE);
    }
}
